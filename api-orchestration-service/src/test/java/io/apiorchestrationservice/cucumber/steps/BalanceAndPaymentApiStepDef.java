package io.apiorchestrationservice.cucumber.steps;

import static io.apiorchestrationservice.testhelpers.apiexecutor.BalanceAndPaymentApiExecutor.*;
import static io.apiorchestrationservice.testhelpers.contextholder.BalanceAndPaymentContextHolder.*;
import static io.apiorchestrationservice.testhelpers.contextholder.QueueTokenContextHolder.*;
import static io.apiorchestrationservice.testhelpers.contextholder.ReservationAndConcertContextHolder.*;
import static io.apiorchestrationservice.testhelpers.parser.BalanceAndPaymentResponseParser.*;
import static io.apiorchestrationservice.util.FieldMapper.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;

import io.apiorchestrationservice.api.application.dto.request.PaymentProcessRequest;
import io.apiorchestrationservice.api.application.dto.request.UserBalanceChargeRequest;
import io.apiorchestrationservice.api.application.dto.response.PaymentResponse;
import io.apiorchestrationservice.api.application.dto.response.UserBalanceChargeResponse;
import io.apiorchestrationservice.api.application.dto.response.UserBalanceSearchResponse;
import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

/**
 * @author : Rene Choi
 * @since : 2024/07/10
 */
public class BalanceAndPaymentApiStepDef implements En {



	@Autowired
	public BalanceAndPaymentApiStepDef(
	) {


		When("사용자의 id가 {long}이고 충전 금액이 {long}인 경우 잔액을 충전 요청하고 정상 응답을 받는다", this::chargeBalanceRequestedWithSuccessResponse);
		Then("사용자의 충전 잔액은 기존의 잔액에 {long}이 추가되어야 한다", this::verifyChargeUserBalance);
		When("사용자의 id가 {long}이고 추가 충전 금액이 {long}인 경우 잔액을 추가 충전 요청하고 정상 응답을 받는다", this::chargeAdditionalBalanceRequestedWithSuccessResponse);

		When("사용자의 초기 잔액을 저장한다", this::storeInitialBalance);
		When("사용자의 충전 후 잔액을 저장한다", this::storeChargedBalance);

		When("사용자의 id가 {long}인 경우 잔액을 조회 요청하고 정상 응답을 받는다", this::searchBalanceRequestedWithSuccessResponse);
		When("사용자의 id가 {long}인 경우 잔액을 조회 요청하면 204 응답을 받는다", this::searchBalanceRequestedWith204Response);

		Given("결제 요청이 다음과 같이 주어졌을 때", this::givenPaymentRequest);
		Given("가장 최근의 예약에 대한 결제 요청이 다음과 같이 주어졌을 때", this::givenPaymentWithMostRecentReservation);
		When("결제 요청을 처리하고 정상 응답을 받는다", this::processPaymentRequestedWithSuccessResponse);
		Then("결제 상태는 {word}여야 한다", this::verifyPaymentStatus);
		Then("결제 내역이 저장되어야 한다", this::verifyPaymentHistory);
		When("결제 요청을 처리하면 실패 응답을 받는다", this::processPaymentRequestedWithFailureResponse);
		Then("조회한 사용자의 잔액은 1000이 차감되어 있어야 한다", this::verifyChargeUserBalance);

		Then("최종 사용자의 잔액은 충전된 잔액에서 사용한 잔액 금액 {long}이 차감되어 반영되어 있어야 한다", this::verifyFinalBalanceAfterDeduction);
		Then("최종 사용자의 잔액은 충전 잔액 대비 차감되지 않은 상태여야 한다", this::verifyFinalBalanceNoDeduction);

	}



	private void chargeBalanceRequestedWithSuccessResponse(Long id, Long amount) {
		ExtractableResponse<Response> response = chargeUserBalanceWithOk(new UserBalanceChargeRequest(id, BigDecimal.valueOf(amount)));
		putChargeResponse(id, parseUserBalanceChargeResponse(response));
	}

	private void chargeAdditionalBalanceRequestedWithSuccessResponse(Long id, Long additionalAmount) {
		chargeBalanceRequestedWithSuccessResponse(id, additionalAmount);
	}

	private void searchBalanceRequestedWithSuccessResponse(Long userId) {
		UserBalanceSearchResponse searchResponse = parseUserBalanceSearchResponse(getUserBalanceWithOk(userId));
		putSearchResponse(userId, searchResponse);
	}

	private void searchBalanceRequestedWith204Response(Long userId) {
		ExtractableResponse<Response> response = getUserBalance(userId);
		assertEquals(204, response.statusCode());
	}

	private void storeInitialBalance() {
		UserBalanceSearchResponse initialBalance = getSearchResponse(getMostRecentUserId());
		putInitialBalance(getMostRecentUserId(), initialBalance);
	}

	private void storeChargedBalance() {
		UserBalanceChargeResponse chargeResponse = getChargeResponse(getMostRecentUserId());
		putChargedBalance(getMostRecentUserId(), chargeResponse);
	}

	private void givenPaymentRequest(DataTable dataTable) {
		putPaymentRequest(updateFields(new PaymentProcessRequest(), dataTable.asMaps().get(0)));
	}

	private void givenPaymentWithMostRecentReservation(DataTable dataTable) {
		PaymentProcessRequest paymentProcessRequest = updateFields(new PaymentProcessRequest(), dataTable.asMaps().get(0));
		paymentProcessRequest.setReservationId(getMostRecentReservationCreateResponse().temporaryReservationId());
		putPaymentRequest(paymentProcessRequest);
	}

	private void processPaymentRequestedWithSuccessResponse() {
		PaymentProcessRequest request = getMostRecentPaymentRequest();
		ExtractableResponse<Response> response = processPaymentWithToken(request, getToken(getMostRecentUserId()));
		assertEquals(200, response.statusCode());
		putPaymentResponse(request.getUserId(), parsePaymentResponse(response));
	}

	private void processPaymentRequestedWithFailureResponse() {
		ExtractableResponse<Response> response = processPayment(getMostRecentPaymentRequest());
		assertNotEquals(200, response.statusCode());
	}

	private void verifyChargeUserBalance(Long expectedBalance) {
		UserBalanceSearchResponse searchResponse = getSearchResponse(getMostRecentUserId());
		UserBalanceChargeResponse chargeResponse = getChargeResponse(getMostRecentUserId());
		assertEquals(expectedBalance.longValue(), chargeResponse.amount().longValue() - searchResponse.amount().longValue(), "잔액이 예상 값과 다릅니다.");
	}

	private void verifySearchUserBalanceIsDeducted(Long deductedBalance) {
		UserBalanceSearchResponse searchResponse = getSearchResponse(getMostRecentUserId());
		assertEquals(deductedBalance.longValue(), searchResponse.amount().longValue() - deductedBalance.longValue(), "잔액이 예상 값과 다릅니다.");
	}

	private void verifyPaymentStatus(String expectedStatus) {
		PaymentResponse paymentResponse = getMostRecentPaymentResponse();
		assertEquals(expectedStatus, paymentResponse.paymentStatus(), "결제 상태가 예상 값과 다릅니다.");
	}

	private void verifyPaymentHistory() {
		PaymentResponse paymentResponse = getMostRecentPaymentResponse();
		assertNotNull(paymentResponse, "결제 내역이 저장되지 않았습니다.");
	}

	private void verifyFinalBalanceAfterDeduction(long deductedAmount) {
		UserBalanceChargeResponse chargedBalance = getChargedBalance(getMostRecentUserId());
		UserBalanceSearchResponse finalBalance = getSearchResponse(getMostRecentUserId());
		assertEquals(chargedBalance.amount().longValue() - deductedAmount, finalBalance.amount().longValue(), "최종 잔액이 예상 값과 다릅니다.");
	}

	private void verifyFinalBalanceNoDeduction() {
		UserBalanceChargeResponse chargedBalance = getChargedBalance(getMostRecentUserId());
		UserBalanceSearchResponse finalBalance = getSearchResponse(getMostRecentUserId());
		assertEquals(chargedBalance.amount(), finalBalance.amount(), "최종 잔액이 예상 값과 다릅니다.");
	}
}

