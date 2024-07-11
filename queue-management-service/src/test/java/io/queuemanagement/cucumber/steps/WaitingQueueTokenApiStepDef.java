package io.queuemanagement.cucumber.steps;

import static io.queuemanagement.cucumber.contextholder.WaitingQueueTokenContextHolder.*;
import static io.queuemanagement.cucumber.utils.fieldmatcher.ResponseMatcher.*;
import static io.queuemanagement.testhelpers.apiexecutor.WaitingQueueTokenApiExecutor.*;
import static io.queuemanagement.testhelpers.parser.WaitingQueueTokenResponseParser.*;
import static io.queuemanagement.util.CustomFieldMapper.*;
import static io.queuemanagement.util.FieldMapper.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.queuemanagement.api.application.dto.request.WaitingQueueTokenGenerateRequest;
import io.queuemanagement.api.application.dto.response.WaitingQueueTokenGeneralResponse;
import io.queuemanagement.api.application.dto.response.WaitingQueueTokenGenerationResponse;

/**
 * @author : Rene Choi
 * @since : 2024/07/06
 */
public class WaitingQueueTokenApiStepDef implements En {

	public WaitingQueueTokenApiStepDef() {
		initFields();

		Given("다음과 같은 유저 정보가 주어지고 대기열 토큰 생성을 요청하면 성공 응답을 받는다", this::givenUserInfoWithDataTableAndGenerateTokenWithSuccessResponse);
		Given("다음과 같은 필수 필드가 누락된 유저 정보가 주어지고 대기열 토큰 생성을 요청하면 실패 응답을 받는다", this::givenUserInfoWithMissingFieldsAndGenerateTokenWithFailureResponse);
		Given("다음과 같은 유저 정보가 주어지고 대기열 토큰 생성을 요청하면 실패 응답을 받는다", this::givenUserInfoAndGenerateTokenWithFailureResponse);

		And("생성된 대기열 토큰의 성공 응답을 조회하면 아래와 같은 정보가 확인되어야 한다", this::verifyTokenInfo);



		Given("다음과 같은 유저 아이디로 대기열 토큰 조회를 요청하면 성공 응답을 받는다", this::givenUserIdAndRetrieveTokenWithSuccessResponse);
		Then("조회된 대기열 토큰의 정보가 아래와 같이 확인되어야 한다", this::verifyRetrievedTokenInfo);



	}

	private void givenUserInfoWithDataTableAndGenerateTokenWithSuccessResponse(DataTable dataTable) {
		Map<String, String> userInfoMap = dataTable.asMaps().get(0);

		WaitingQueueTokenGenerateRequest tokenGenerateRequest = applyCustomMappings(updateFields(new WaitingQueueTokenGenerateRequest(), userInfoMap), userInfoMap);

		putWaitingQueueTokenGenerationResponse(parseWaitingQueueTokenGenerationResponse(generateWaitingQueueToken(tokenGenerateRequest)));
		putWaitingQueueTokenGenerateRequest(tokenGenerateRequest);
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

		assertEquals(400, generateWaitingQueueToken(tokenGenerateRequest).statusCode());
	}




	private void givenUserIdAndRetrieveTokenWithSuccessResponse(DataTable dataTable) {
		Map<String, String> userIdMap = dataTable.asMaps().get(0);
		String  userId = userIdMap.get("userId");

		putWaitingQueueTokenGeneralResponse(parseWaitingQueueTokenGeneralResponse(retrieveWaitingQueueToken(userId)));
	}

	private void verifyRetrievedTokenInfo(DataTable dataTable) {
		List<Map<String, String>> expectedTokens = dataTable.asMaps(String.class, String.class);
		WaitingQueueTokenGeneralResponse actualResponse = getMostRecentGeneralResponse();
		assertNotNull(actualResponse, "조회된 대기열 토큰의 응답이 존재하지 않습니다.");

		boolean matchFound = expectedTokens.stream().anyMatch(expectedToken -> matchResponse(expectedToken, actualResponse));

		assertTrue(matchFound, "기대한 대기열 토큰 정보가 일치하지 않습니다.");
	}
}
