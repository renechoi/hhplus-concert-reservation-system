package io.queuemanagement.common.logtrace.filter;

import static io.queuemanagement.testhelpers.apiexecutor.WaitingQueueTokenApiExecutor.*;
import static io.queuemanagement.util.YmlLoader.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.LongStream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.RepeatedTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import io.queuemanagement.api.application.dto.request.WaitingQueueTokenGenerateRequest;
import io.queuemanagement.testhelpers.apiexecutor.DynamicPortHolder;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Slf4j
@TestPropertySource(properties = {
	"custom-logging.filter.enabled=true"
})
@Disabled
class LoggingFilterIntegrationTest {

	@LocalServerPort
	private int port;

	private static List<Long> durationsWithFilter = new ArrayList<>();
	private static List<Long> durationsWithoutFilter = new ArrayList<>();

	@BeforeEach
	public void setUp() {
		DynamicPortHolder.setPort(port);
		ymlLoader().backup();
	}

	@AfterEach
	public void tearDown() {
		ymlLoader().restore();
	}

	@AfterAll
	static void tearDownAll() {
		// 최종 결과 로그 출력
		logFinalResults();
	}

	@SneakyThrows
	@RepeatedTest(50)
	void testPerformance() {
		int iterations = 10;

		// 예열
		for (int i = 0; i < 5; i++) {
			measureRequestDuration(createSampleRequestWithLargeBody());
		}

		Thread.sleep(100);
		long totalDurationWithFilter = LongStream.range(0, iterations)
			.map(i -> measureRequestDuration(createSampleRequestWithLargeBody()))
			.sum();
		long averageDurationWithFilter = totalDurationWithFilter / iterations;
		durationsWithFilter.add(averageDurationWithFilter);

		setLoggingFilterEnabled(false);
		Thread.sleep(100);

		long totalDurationWithoutFilter = LongStream.range(0, iterations)
			.map(i -> measureRequestDuration(createSampleRequestWithLargeBody()))
			.sum();
		long averageDurationWithoutFilter = totalDurationWithoutFilter / iterations;
		durationsWithoutFilter.add(averageDurationWithoutFilter);

		assertThat(averageDurationWithFilter).isGreaterThan(0);
		assertThat(averageDurationWithoutFilter).isGreaterThan(0);
	}

	private long measureRequestDuration(WaitingQueueTokenGenerateRequest request) {
		long startTime = System.currentTimeMillis();

		ExtractableResponse<Response> response = generateWaitingQueueToken(request);

		long endTime = System.currentTimeMillis();
		long duration = endTime - startTime;

		assertThat(response.statusCode()).isEqualTo(500);
		return duration;
	}

	private void setLoggingFilterEnabled(boolean enabled) {
		ymlLoader().setLoggingFilterEnabled(enabled);
	}

	private WaitingQueueTokenGenerateRequest createSampleRequestWithLargeBody() {
		WaitingQueueTokenGenerateRequest request = WaitingQueueTokenGenerateRequest.builder()
			.userId("A".repeat(10000))
			.priority(1)
			.waitingQueueId(1L)
			.build();
		request.setRequestAt(LocalDateTime.now());
		return request;
	}

	private static void logFinalResults() {
		log.info("성능 테스트 결과:");
		log.info("필터 사용:");
		for (int i = 0; i < durationsWithFilter.size(); i++) {
			log.info("테스트 " + (i + 1) + ": " + durationsWithFilter.get(i) + "ms");
		}
		long averageWithFilter = durationsWithFilter.stream().mapToLong(Long::longValue).sum() / durationsWithFilter.size();
		log.info("필터 사용 시 평균 시간: " + averageWithFilter + "ms");

		log.info("필터 미사용:");
		for (int i = 0; i < durationsWithoutFilter.size(); i++) {
			log.info("테스트 " + (i + 1) + ": " + durationsWithoutFilter.get(i) + "ms");
		}
		long averageWithoutFilter = durationsWithoutFilter.stream().mapToLong(Long::longValue).sum() / durationsWithoutFilter.size();
		log.info("필터 미사용 시 평균 시간: " + averageWithoutFilter + "ms");
	}
}
