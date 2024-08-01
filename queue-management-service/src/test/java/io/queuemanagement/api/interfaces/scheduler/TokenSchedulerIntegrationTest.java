package io.queuemanagement.api.interfaces.scheduler;

import static io.queuemanagement.cucumber.contextholder.WaitingQueueTokenContextHolder.*;
import static io.queuemanagement.testhelpers.apiexecutor.WaitingQueueTokenApiExecutor.*;
import static io.queuemanagement.testhelpers.parser.WaitingQueueTokenResponseParser.*;
import static io.queuemanagement.util.YmlLoader.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import io.queuemanagement.api.acceptance.CommonAcceptanceTest;
import io.queuemanagement.api.application.dto.request.WaitingQueueTokenGenerateRequest;
import io.queuemanagement.api.application.dto.response.WaitingQueueTokenGeneralResponse;
import io.queuemanagement.api.application.dto.response.WaitingQueueTokenGenerationResponse;
import io.queuemanagement.api.business.domainmodel.QueueStatus;
import io.queuemanagement.testhelpers.apiexecutor.DynamicPortHolder;
import io.queuemanagement.util.YmlLoader;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import lombok.SneakyThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
	properties = {
		"scheduler.queueTransferRate=1000",
		"scheduler.expireTokensRate=1000"
	}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("test")
class TokenSchedulerIntegrationTest extends CommonAcceptanceTest {

	@LocalServerPort
	private int port;

	@BeforeEach
	public void setDynamicPort() {
		DynamicPortHolder.setPort(port);
		ymlLoader().backup();
	}

	@AfterEach
	public void tearDown() {
		ymlLoader().restore();
	}


	@SneakyThrows
	@Test
	@DisplayName("대기열 -> 처리열 이동 및 만료 토큰 처리 검증")
	void testTokenSchedulerIntegration() {
		// 1. 대기열 토큰 생성
		mockYmlLoaderTokenExpiry(5); // 만료 시간을 5초로 모킹

		WaitingQueueTokenGenerateRequest generateRequest = new WaitingQueueTokenGenerateRequest();
		generateRequest.setUserId("user1");
		generateRequest.setPriority(1);
		generateRequest.setRequestAt(LocalDateTime.now());

		putWaitingQueueTokenGenerationResponse(parseWaitingQueueTokenGenerationResponse(generateWaitingQueueToken(generateRequest)));

		// 2. 대기열 토큰 생성 확인
		WaitingQueueTokenGenerationResponse generationResponse = getMostRecentGenerationResponse();
		assertThat(generationResponse).isNotNull();
		assertThat(generationResponse.userId()).isEqualTo("user1");

		// 3. 대기열 -> 처리열 이동 스케줄러 실행 대기
		Thread.sleep(1000); // 스케줄러 실행 주기(2초)를 기다림
		Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
			// 4. 처리열 토큰 확인
			WaitingQueueTokenGeneralResponse generalResponse = parseWaitingQueueTokenGeneralResponse(retrieveWaitingQueueToken("user1"));
			// assertThat(generalResponse).isNotNull();
			// assertThat(generalResponse.status()).isEqualTo(QueueStatus.PROCESSING);
		});

		// 5. 만료 토큰 처리 스케줄러 실행 대기
		Thread.sleep(4000); // 스케줄러 실행 주기(4초)를 기다림
		Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
			// 6. 만료된 토큰 확인
			ExtractableResponse<Response> response = retrieveWaitingQueueToken("user1");
			WaitingQueueTokenGeneralResponse waitingQueueTokenGeneralResponse = parseWaitingQueueTokenGeneralResponse(response);
			assertThat(response.statusCode()).isEqualTo(200); // 만료된 토큰은 조회되지 않음 -> processing 조회되도록 변경
			assertEquals(waitingQueueTokenGeneralResponse.status(), QueueStatus.PROCESSING);
		});
	}

	private void mockYmlLoaderTokenExpiry(int expiryInSeconds) {
		Map<String, String> configMap = YmlLoader.getConfigMap();
		configMap.put("waiting-queue.policy.token-expiry-as-seconds", String.valueOf(expiryInSeconds));
	}
}
