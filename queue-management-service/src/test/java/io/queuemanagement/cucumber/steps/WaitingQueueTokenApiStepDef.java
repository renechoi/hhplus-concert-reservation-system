package io.queuemanagement.cucumber.steps;

import static io.queuemanagement.cucumber.contextholder.WaitingQueueTokenContextHolder.*;
import static io.queuemanagement.cucumber.utils.fieldmatcher.ResponseMatcher.*;
import static io.queuemanagement.testhelpers.apiexecutor.WaitingQueueTokenApiExecutor.*;
import static io.queuemanagement.testhelpers.parser.WaitingQueueTokenResponseParser.*;
import static io.queuemanagement.util.CustomFieldMapper.*;
import static io.queuemanagement.util.FieldMapper.*;
import static io.queuemanagement.util.UrlEncodingHelper.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.queuemanagement.api.application.dto.request.WaitingQueueTokenGenerateRequest;
import io.queuemanagement.api.application.dto.response.WaitingQueueTokenGeneralResponse;
import io.queuemanagement.api.application.dto.response.WaitingQueueTokenGenerationResponse;
import io.queuemanagement.api.infrastructure.clients.redisservice.RedisServiceClient;
import io.queuemanagement.api.infrastructure.persistence.orm.WaitingQueueTokenJpaRepository;
import io.restassured.internal.RestAssuredResponseImpl;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import lombok.SneakyThrows;

/**
 * @author : Rene Choi
 * @since : 2024/07/06
 */
public class WaitingQueueTokenApiStepDef implements En {

	private WaitingQueueTokenJpaRepository waitingQueueTokenJpaRepository;
	private  RedisServiceClient redisServiceClient;

	@Autowired
	public WaitingQueueTokenApiStepDef(
		WaitingQueueTokenJpaRepository waitingQueueTokenJpaRepository,
		RedisServiceClient redisServiceClient
	) {
		this.waitingQueueTokenJpaRepository = waitingQueueTokenJpaRepository;
		this.redisServiceClient = redisServiceClient;
		initFields();

		Given("레디스 데이터 초기화", this::clearRedisData);

		Given("다음과 같은 유저 정보가 주어지고 대기열 토큰 생성을 요청하면 성공 응답을 받는다", this::givenUserInfoWithDataTableAndGenerateTokenWithSuccessResponse);
		Given("다음과 같은 필수 필드가 누락된 유저 정보가 주어지고 대기열 토큰 생성을 요청하면 실패 응답을 받는다", this::givenUserInfoWithMissingFieldsAndGenerateTokenWithFailureResponse);
		Given("다음과 같은 유저 정보가 주어지고 대기열 토큰 생성을 요청하면 실패 응답을 받고 예외 메시지를 확인한다", this::givenUserInfoAndGenerateTokenWithFailureResponse);
		Given("{int}명의 random 유저 정보가 주어지고 대기열 토큰 생성을 동시에 요청하면 성공 응답을 받는다", this::givenRandomUserInfoAndGenerateTokensWithSuccessResponse);

		Given("다음과 같은 유저 아이디로 대기열 토큰 조회를 요청하면 성공 응답을 받는다", this::givenUserIdAndRetrieveTokenWithSuccessResponse);
		Given("다음과 같은 유저 아이디로 대기열 토큰 조회를 요청하면 실패 응답을 받고 예외 메시지를 확인한다", this::givenUserIdAndRetrieveTokenWithFailureResponse);
		Given("다음과 같은 유저 아이디로 대기열 토큰 전체 조회를 요청하면 성공 응답을 받고 그 개수는 {int}개여야 한다", this::givenUserIdAndRetrieveAllTokensWithSuccessResponseWithCounts);
		Given("다음과 같은 유저 정보가 주어지고 대기열 토큰 생성을 {int}번 동시에 요청하면 성공 응답을 받는다", this::givenSingleUserInfoAndGenerateTokenMultipleTimesWithSuccessResponse);
		Then("생성된 각 대기열 토큰의 성공 응답을 조회하면 아래와 같은 정보가 확인되어야 한다", this::verifyMultipleTokensInfo);
		Then("생성된 각 대기열 토큰의 성공 응답을 조회하면 각기 다른 포지션 정보가 확인되어야 한다 - 순서 정합성 보장", this::verifyTokensWithDifferentPositions);

		Then("조회된 대기열 토큰의 정보가 아래와 같이 확인되어야 한다", this::verifyRetrievedTokenInfo);
		And("생성된 대기열 토큰의 성공 응답을 조회하면 아래와 같은 정보가 확인되어야 한다", this::verifyTokenInfo);
		When("다음과 같은 유저 아이디로 대기열 토큰 조회를 요청하고 제시된 응답을 받는다", this::retrieveTokenWithExpectedStatusCode);
		Then("생성된 대기열 토큰의 성공 응답을 조회하면 하나의 토큰만 생성되어야 한다", this::verifySingleTokenInfo);

	}

	private void clearRedisData() {
		redisServiceClient.clearAllData();
	}

	private void givenUserInfoWithDataTableAndGenerateTokenWithSuccessResponse(DataTable dataTable) {
		Map<String, String> userInfoMap = dataTable.asMaps().get(0);

		WaitingQueueTokenGenerateRequest tokenGenerateRequest = applyCustomMappings(updateFields(new WaitingQueueTokenGenerateRequest(), userInfoMap), userInfoMap);

		putWaitingQueueTokenGenerationResponse(parseWaitingQueueTokenGenerationResponse(generateWaitingQueueToken(tokenGenerateRequest)));
		putWaitingQueueTokenGenerateRequest(tokenGenerateRequest);
	}


	private void givenRandomUserInfoAndGenerateTokensWithSuccessResponse(int userCount)  {
		List<Map<String, String>> usersInfoList = generateRandomUserInfo(userCount);
		ExecutorService executorService = Executors.newFixedThreadPool(userCount);
		CountDownLatch latch = new CountDownLatch(1);
		CountDownLatch doneLatch = new CountDownLatch(userCount);

		for (Map<String, String> userInfo : usersInfoList) {
			executorService.submit(() -> {
				try {
					latch.await();
					WaitingQueueTokenGenerateRequest tokenGenerateRequest = applyCustomMappings(updateFields(new WaitingQueueTokenGenerateRequest(), userInfo), userInfo);
					ExtractableResponse<Response> response = generateWaitingQueueToken(tokenGenerateRequest);
					putWaitingQueueTokenGenerationResponse(parseWaitingQueueTokenGenerationResponse(response));
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				} finally {
					doneLatch.countDown();
				}
			});
		}

		latch.countDown(); // 모든 스레드가 준비되면 동시에 시작
		awaitTermination(executorService, doneLatch);
	}


	private List<Map<String, String>> generateRandomUserInfo(int count) {
		Random random = new Random();
		return IntStream.range(0, count)
			.mapToObj(i -> Map.of(
				"userId", "user" + random.nextInt(100000000),
				"priority", "1",
				"requestAt", "now"
			))
			.collect(Collectors.toList());
	}


	private void givenSingleUserInfoAndGenerateTokenMultipleTimesWithSuccessResponse(int repeatCount, DataTable dataTable)  {
		Map<String, String> userInfoMap = dataTable.asMaps().get(0);
		ExecutorService executorService = Executors.newFixedThreadPool(repeatCount);
		CountDownLatch latch = new CountDownLatch(1);
		CountDownLatch doneLatch = new CountDownLatch(repeatCount);

		for (int i = 0; i < repeatCount; i++) {
			executorService.submit(() -> {
				try {
					latch.await();
					ExtractableResponse<Response> response = generateWaitingQueueTokenWithCreated(applyCustomMappings(updateFields(new WaitingQueueTokenGenerateRequest(), userInfoMap), userInfoMap));
					Assertions.assertEquals(201, response.statusCode());
					putWaitingQueueTokenGenerationResponse(parseWaitingQueueTokenGenerationResponse(response));
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				} finally {
					doneLatch.countDown();
				}
			});
		}

		latch.countDown(); // 모든 스레드가 준비되면 동시에 시작
		awaitTermination(executorService, doneLatch);
	}

	private void retrieveTokenWithExpectedStatusCode(DataTable dataTable) {
		Map<String, String> userIdMap = dataTable.asMaps().get(0);
		String userId = userIdMap.get("userId");
		int expectedStatusCode = Integer.parseInt(userIdMap.get("statusCode"));

		int statusCode = retrieveWaitingQueueToken(userId).statusCode();
		assertEquals(expectedStatusCode, statusCode, "응답 코드가 일치하지 않습니다.");
	}

	private void verifyTokenInfo(DataTable dataTable) {
		List<Map<String, String>> expectedTokens = dataTable.asMaps(String.class, String.class);
		WaitingQueueTokenGenerationResponse actualResponse = getMostRecentGenerationResponse();
		assertNotNull(actualResponse, "생성된 대기열 토큰의 응답이 존재하지 않습니다.");

		boolean matchFound = expectedTokens.stream().anyMatch(expectedToken -> matchResponse(expectedToken, actualResponse));

		assertTrue(matchFound, "기대한 대기열 토큰 정보가 일치하지 않습니다.");
	}

	private void givenUserInfoWithMissingFieldsAndGenerateTokenWithFailureResponse(DataTable dataTable) {
		Map<String, String> userInfoMap = dataTable.asMaps().get(0);

		WaitingQueueTokenGenerateRequest tokenGenerateRequest = applyCustomMappings(updateFields(new WaitingQueueTokenGenerateRequest(), userInfoMap), userInfoMap);

		assertEquals(400, generateWaitingQueueToken(tokenGenerateRequest).statusCode());
	}

	private void givenUserInfoAndGenerateTokenWithFailureResponse(DataTable dataTable) {
		Map<String, String> userInfoMap = dataTable.asMaps().get(0);

		WaitingQueueTokenGenerateRequest tokenGenerateRequest = applyCustomMappings(updateFields(new WaitingQueueTokenGenerateRequest(), userInfoMap), userInfoMap);

		ExtractableResponse<Response> response = generateWaitingQueueToken(tokenGenerateRequest);


		assertEquals(Integer.parseInt(userInfoMap.get("statusCode")), response.statusCode(), "응답 코드가 일치하지 않습니다.");
		assertEquals(userInfoMap.get("message"),  base64Decode(((RestAssuredResponseImpl) response).getHeader("Response-Message")), "응답 메시지가 일치하지 않습니다.");
	}


	private void givenUserIdAndRetrieveTokenWithSuccessResponse(DataTable dataTable) {
		Map<String, String> userIdMap = dataTable.asMaps().get(0);
		String userId = userIdMap.get("userId");

		putWaitingQueueTokenGeneralResponse(parseWaitingQueueTokenGeneralResponse(retrieveWaitingQueueToken(userId)));
	}

	private void givenUserIdAndRetrieveTokenWithFailureResponse(DataTable dataTable) {
		Map<String, String> userIdMap = dataTable.asMaps().get(0);
		String userId = userIdMap.get("userId");
		int expectedStatusCode = Integer.parseInt(userIdMap.get("statusCode"));
		String expectedMessage = userIdMap.get("message");

		ExtractableResponse<Response> response = retrieveWaitingQueueToken(userId);

		assertEquals(expectedStatusCode, response.statusCode(), "응답 코드가 일치하지 않습니다.");

		String actualMessage = base64Decode(((RestAssuredResponseImpl) response).getHeader("Response-Message"));
		assertEquals(expectedMessage, actualMessage, "응답 메시지가 일치하지 않습니다.");
	}


	private void givenUserIdAndRetrieveAllTokensWithSuccessResponseWithCounts(int counts, DataTable dataTable) {
		Map<String, String> userIdMap = dataTable.asMaps().get(0);
		String userId = userIdMap.get("userId");
		assertEquals(counts, waitingQueueTokenJpaRepository.findAll().stream().filter(item->userId.equals(item.getUserId())).count());
	}

	private void verifyRetrievedTokenInfo(DataTable dataTable) {
		List<Map<String, String>> expectedTokens = dataTable.asMaps(String.class, String.class);
		WaitingQueueTokenGeneralResponse actualResponse = getMostRecentGeneralResponse();
		assertNotNull(actualResponse, "조회된 대기열 토큰의 응답이 존재하지 않습니다.");

		boolean matchFound = expectedTokens.stream().anyMatch(expectedToken -> matchResponse(expectedToken, actualResponse));

		assertTrue(matchFound, "기대한 대기열 토큰 정보가 일치하지 않습니다.");
	}


	private void verifyMultipleTokensInfo(DataTable dataTable) {
		List<Map<String, String>> expectedTokens = dataTable.asMaps(String.class, String.class);
		List<WaitingQueueTokenGenerationResponse> actualResponses = getAllGenerationResponses();

		assertEquals(expectedTokens.size(), actualResponses.size(), "응답된 토큰의 수가 예상과 다릅니다.");

		for (Map<String, String> expectedToken : expectedTokens) {
			boolean matchFound = actualResponses.stream().anyMatch(actualResponse -> matchResponse(expectedToken, actualResponse));
			assertTrue(matchFound, "기대한 대기열 토큰 정보가 일치하지 않습니다: " + expectedToken);
		}
	}


	private void verifyTokensWithDifferentPositions() {
		List<WaitingQueueTokenGenerationResponse> actualResponses = getAllGenerationResponses();

		assertNotNull(actualResponses, "생성된 대기열 토큰의 응답이 존재하지 않습니다.");
		assertFalse(actualResponses.isEmpty(), "생성된 대기열 토큰이 존재하지 않습니다.");

		Set<Integer> positions = actualResponses.stream()
			.map(WaitingQueueTokenGenerationResponse::position)
			.collect(Collectors.toSet());

		assertEquals(actualResponses.size(), positions.size(), "각 유저의 포지션 정보는 각기 달라야 합니다.");
	}


	private void verifySingleTokenInfo() {
		List<WaitingQueueTokenGenerationResponse> actualResponses = getAllGenerationResponses();
		assertEquals(1, actualResponses.size(), "하나의 토큰만 생성되어야 합니다.");
	}

	@SneakyThrows
	private void awaitTermination(ExecutorService executorService, CountDownLatch doneLatch) {
		doneLatch.await(20, TimeUnit.SECONDS);
		executorService.shutdown();
		if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
			executorService.shutdownNow();
		}
	}

}
