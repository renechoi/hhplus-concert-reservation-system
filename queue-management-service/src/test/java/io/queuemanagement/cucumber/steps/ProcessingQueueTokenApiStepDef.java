package io.queuemanagement.cucumber.steps;

import static io.queuemanagement.cucumber.contextholder.ProcessingQueueTokenContextHolder.*;
import static io.queuemanagement.cucumber.contextholder.WaitingQueueTokenContextHolder.*;
import static io.queuemanagement.cucumber.utils.fieldmatcher.ResponseMatcher.*;
import static io.queuemanagement.testhelpers.apiexecutor.ProcessingQueueTokenApiExecutor.*;
import static io.queuemanagement.testhelpers.parser.ProcessingQueueTokenResponseParser.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.queuemanagement.api.application.dto.response.ProcessingQueueTokenGeneralResponse;
import io.queuemanagement.api.application.dto.response.WaitingQueueTokenGeneralResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
public class ProcessingQueueTokenApiStepDef implements En {

	public ProcessingQueueTokenApiStepDef() {


		Given("유저 아이디와 응답받은 대기열 토큰으로 처리열 토큰 유효성을 조회 요청하면 성공 응답을 받는다", this::checkTokenAvailabilityByRetrievedWaitingTokenWithSuccessResponse);
		Given("다음과 같은 유저 아이디와 토큰으로 처리열 토큰 유효성을 조회 요청하면 204 응답을 받는다", this::checkTokenAvailabilityWith204Response);
		Given("잘못된 유저 아이디와 토큰으로 처리열 토큰 유효성을 조회 요청하면 204 응답을 받는다", this::checkTokenAvailabilityByWrongUserIdWith204Response);
		Then("조회된 처리열 토큰의 정보가 아래와 같이 확인되어야 한다", this::verifyCheckedTokenInfo);

	}


	private void checkTokenAvailabilityByRetrievedWaitingTokenWithSuccessResponse(DataTable dataTable) {
		Map<String, String> userIdMap = dataTable.asMaps().get(0);
		String userId = userIdMap.get("userId");
		String tokenValue = userIdMap.get("tokenValue");

		if ("retrieved".equals(tokenValue)) {
			WaitingQueueTokenGeneralResponse retrievedResponse = getWaitingQueueTokenGeneralResponse(userId);
			assertNotNull(retrievedResponse, "조회된 대기열 토큰의 응답이 존재하지 않습니다.");
			tokenValue = retrievedResponse.tokenValue();
		}

		putToken(userId, tokenValue);
		putResponse(userId, parseProcessingQueueTokenGeneralResponse(checkProcessingQueueTokenAvailability(tokenValue, userId)));
	}



	private void checkTokenAvailabilityWith204Response(DataTable dataTable) {
		Map<String, String> userIdMap = dataTable.asMaps().get(0);
		String userId = userIdMap.get("userId");
		String tokenValue = userIdMap.get("tokenValue");

		if ("retrieved".equals(tokenValue)) {
			WaitingQueueTokenGeneralResponse retrievedResponse = getWaitingQueueTokenGeneralResponse(userId);

			assertNotNull(retrievedResponse, "조회된 대기열 토큰의 응답이 존재하지 않습니다.");
			tokenValue = retrievedResponse.tokenValue();
		}

		ExtractableResponse<Response> response = checkProcessingQueueTokenAvailability(tokenValue, userId);
		assertEquals(204, response.statusCode(), "응답 코드가 예상과 다릅니다.");
	}

	private void checkTokenAvailabilityByWrongUserIdWith204Response(DataTable dataTable) {
		Map<String, String> userIdMap = dataTable.asMaps().get(0);
		String userId = userIdMap.get("userId");
		String tokenValue = userIdMap.get("tokenValue");

		if ("retrieved".equals(tokenValue)) {
			WaitingQueueTokenGeneralResponse retrievedResponse = getMostRecentGeneralResponse();

			assertNotNull(retrievedResponse, "조회된 대기열 토큰의 응답이 존재하지 않습니다.");
			tokenValue = retrievedResponse.tokenValue();
		}

		ExtractableResponse<Response> response = checkProcessingQueueTokenAvailability(tokenValue, userId);
		assertEquals(204, response.statusCode(), "응답 코드가 예상과 다릅니다.");
	}


	private void verifyCheckedTokenInfo(DataTable dataTable) {
		List<Map<String, String>> expectedTokens = dataTable.asMaps(String.class, String.class);
		ProcessingQueueTokenGeneralResponse actualResponse = getMostRecentResponse();
		assertNotNull(actualResponse, "조회된 처리열 토큰의 응답이 존재하지 않습니다.");

		boolean matchFound = expectedTokens.stream().anyMatch(expectedToken -> matchResponse(expectedToken, actualResponse));

		assertTrue(matchFound, "기대한 처리열 토큰 정보가 일치하지 않습니다.");
	}





}