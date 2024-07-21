package io.paymentservice.cucumber.steps;

import static io.paymentservice.testhelpers.apiexecutor.BalanceApiExecutor.*;
import static io.paymentservice.testhelpers.contextholder.BalanceContextHolder.*;
import static io.paymentservice.testhelpers.parser.BalanceResponseParser.*;
import static org.junit.jupiter.api.Assertions.*;

import io.cucumber.java8.En;
import io.paymentservice.api.balance.interfaces.dto.response.BalanceSearchResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

/**
 * @author : Rene Choi
 * @since : 2024/07/10
 */

public class BalanceSearchApiStepDef implements En {

	public BalanceSearchApiStepDef() {
		When("사용자의 id가 {long}인 경우 잔액을 조회 요청하고 정상 응답을 받는다", this::searchBalanceRequestedWithSuccessResponse);
		Then("조회한 사용자의 잔액은 {long}이어야 한다", this::verifyBalance);

		When("사용자의 id가 {long}인 경우 잔액을 조회 요청하면 예외가 발생한다", this::searchBalanceRequestedWithFailureResponse);
		When("사용자의 id가 {long}인 경우 잔액을 조회 요청하면 204 응답을 받는다", this::searchBalanceRequestedWith204Response);
	}



	private void searchBalanceRequestedWithSuccessResponse(Long userId) {
		ExtractableResponse<Response> response = getBalance(userId);
		assertEquals(200, response.statusCode());
		BalanceSearchResponse searchResponse = parseBalanceSearchResponse(response);
		putSearchResponse(userId, searchResponse);
	}

	private void searchBalanceRequestedWithFailureResponse(Long userId) {
		ExtractableResponse<Response> response = getBalance(userId);
		assertNotEquals(200, response.statusCode());
	}

	private void searchBalanceRequestedWith204Response(Long userId) {
		ExtractableResponse<Response> response = getBalance(userId);
		assertEquals(204, response.statusCode());
	}

	private void verifyBalance(Long expectedBalance) {
		BalanceSearchResponse searchResponse = getSearchResponse(getMostRecentUserId());
		assertEquals(expectedBalance, searchResponse.amount().longValue(), "조회한 잔액이 예상 값과 다릅니다.");
	}


}
