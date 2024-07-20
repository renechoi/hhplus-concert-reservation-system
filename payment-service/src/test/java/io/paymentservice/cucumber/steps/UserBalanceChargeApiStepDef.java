package io.paymentservice.cucumber.steps;

import static io.paymentservice.testhelpers.apiexecutor.UserBalanceApiExecutor.*;
import static io.paymentservice.testhelpers.contextholder.UserBalanceContextHolder.*;
import static io.paymentservice.testhelpers.parser.UserBalanceResponseParser.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import io.cucumber.java8.En;
import io.paymentservice.api.balance.interfaces.dto.request.UserBalanceChargeRequest;
import io.paymentservice.api.balance.interfaces.dto.response.UserBalanceChargeResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

/**
 * @author : Rene Choi
 * @since : 2024/07/09
 */

public class UserBalanceChargeApiStepDef implements En {

	public UserBalanceChargeApiStepDef() {
		When("사용자의 id가 {long}이고 충전 금액이 {long}인 경우 잔액을 충전 요청하고 정상 응답을 받는다", this::chargeBalanceRequestedWithSuccessResponse);
		When("사용자의 id가 {long}이고 추가 충전 금액이 {long}인 경우 잔액을 추가 충전 요청하고 정상 응답을 받는다", this::chargeAdditionalBalanceRequestedWithSuccessResponse);
		Then("사용자의 잔액은 {long}이어야 한다", this::verifyUserBalance);

		// 예외 및 제약 조건
		When("사용자의 id가 {long}이고 충전 금액이 {long}인 경우 잔액을 충전 요청하면 예외가 발생한다", this::chargeBalanceRequestedWithFailureResponse);
		When("{long} 잔액을 충전 요청하면 예외가 발생한다", this::chargeBalanceExceedsLimitRequestedWithFailureResponse);
	}

	private void chargeBalanceRequestedWithSuccessResponse(Long id, Long amount) {
		ExtractableResponse<Response> response = chargeUserBalance(id, new UserBalanceChargeRequest(id, BigDecimal.valueOf(amount)));
		assertEquals(200, response.statusCode());
		putChargeResponse(id, parseUserBalanceChargeResponse(response));
	}

	private void chargeAdditionalBalanceRequestedWithSuccessResponse(Long id, Long additionalAmount) {
		ExtractableResponse<Response> response = chargeUserBalance(id, new UserBalanceChargeRequest(id, BigDecimal.valueOf(additionalAmount)));
		assertEquals(200, response.statusCode());
		putChargeResponse(id, parseUserBalanceChargeResponse(response));
	}

	private void verifyUserBalance(Long expectedBalance) {
		UserBalanceChargeResponse chargeResponse = getMostRecentChargeResponse();
		assertEquals(expectedBalance, chargeResponse.amount().longValue(), "충전 후 잔액이 예상 값과 다릅니다.");
	}

	private void chargeBalanceRequestedWithFailureResponse(Long id, Long amount) {
		UserBalanceChargeRequest chargeRequest = new UserBalanceChargeRequest(id, BigDecimal.valueOf(amount));
		assertNotEquals(200, chargeUserBalance(id, chargeRequest).statusCode());
	}


	private void chargeBalanceExceedsLimitRequestedWithFailureResponse(Long amount) {
		Long userId = getMostRecentUserId();
		ExtractableResponse<Response> response = chargeUserBalance(userId,  new UserBalanceChargeRequest(userId, BigDecimal.valueOf(amount)));
		assertNotEquals(200, response.statusCode());
	}


}