package io.paymentservice.cucumber.steps;

import static io.paymentservice.api.payment.business.entity.PaymentStatus.*;
import static io.paymentservice.testhelpers.apiexecutor.PaymentApiExecutor.*;
import static io.paymentservice.testhelpers.contextholder.PaymentContextHolder.*;
import static io.paymentservice.testhelpers.parser.PaymentResponseParser.*;
import static io.paymentservice.util.FieldMapper.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.paymentservice.api.payment.business.entity.PaymentTransaction;
import io.paymentservice.api.payment.infrastructure.orm.PaymentTransactionJpaRepository;
import io.paymentservice.api.payment.interfaces.dto.response.PaymentResponse;
import io.paymentservice.api.payment.interfaces.dto.response.PaymentResponses;
import io.paymentservice.testhelpers.fieldmatcher.ResponseMatcher;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

/**
 * Author: Rene Choi
 * Since: 2024/07/10
 */
public class PaymentHistoryRetrievalApiStepDef implements En {

	private PaymentTransactionJpaRepository paymentTransactionJpaRepository;
	@Autowired
	public PaymentHistoryRetrievalApiStepDef(PaymentTransactionJpaRepository paymentTransactionJpaRepository) {
		this.paymentTransactionJpaRepository= paymentTransactionJpaRepository;

		Given("사용자의 id가 {long}이고 결제 내역이 다음과 같이 존재할 때", this::givenUserWithPaymentHistory);
		When("사용자의 id가 {long}인 경우 결제 내역을 조회 요청하고 정상 응답을 받는다", this::retrievePaymentHistoryForUser);
		Then("조회한 결제 내역은 다음과 같아야 한다", this::verifyRetrievedPaymentHistory);
		When("사용자의 id가 {long}인 경우 실패한 결제 내역을 조회 요청하고 정상 응답을 받는다", this::retrieveFailedPaymentHistoryForUser);
		Then("조회한 실패한 결제 내역은 다음과 같아야 한다", this::verifyRetrievedFailedPaymentHistory);
		When("사용자의 id가 {long}인 경우 결제 내역을 조회 요청하면 {int} 응답을 받는다", this::retrievePaymentHistoryForNonExistentUser);
		Then("조회한 결제 내역은 비어 있어야 한다", this::verifyEmptyPaymentHistory);
	}

	private void givenUserWithPaymentHistory(Long userId, DataTable dataTable) {
		for (Map<String, String> data : dataTable.asMaps()) {
			paymentTransactionJpaRepository.save(updateFields(new PaymentTransaction(), data));
		}
	}

	private void retrievePaymentHistoryForUser(Long userId) {
		putPaymentHistory(userId, parsePaymentHistoryResponse(retrievePaymentHistoryWithOk(userId)));
	}

	private void verifyRetrievedPaymentHistory(DataTable expectedDataTable) {
		List<Map<String, String>> expectedData = expectedDataTable.asMaps();
		PaymentResponses paymentResponses = getPaymentHistory(Long.valueOf(expectedData.get(0).get("userId")));

		assertNotNull(paymentResponses, "결제 내역이 존재해야 합니다.");
		List<PaymentResponse> paymentResponseList = paymentResponses.paymentResponses();
		assertEquals(expectedData.size(), paymentResponseList.size(), "결제 내역의 개수가 일치해야 합니다.");

		for (int i = 0; i < expectedData.size(); i++) {
			Map<String, String> expected = expectedData.get(i);
			PaymentResponse actual = paymentResponseList.get(i);

			boolean isMatch = ResponseMatcher.matchResponse(expected, actual);
			assertTrue(isMatch, "결제 내역이 일치해야 합니다.");
		}
	}

	private void retrieveFailedPaymentHistoryForUser(Long userId) {
		ExtractableResponse<Response> response = retrievePaymentHistoryWithOk(userId);
		putPaymentHistory(userId, parsePaymentHistoryResponse(response));
	}

	private void verifyRetrievedFailedPaymentHistory(DataTable expectedDataTable) {
		List<Map<String, String>> expectedData = expectedDataTable.asMaps();
		PaymentResponses paymentResponses = getPaymentHistory(Long.valueOf(expectedData.get(0).get("userId")));

		assertNotNull(paymentResponses, "결제 내역이 존재해야 합니다.");
		List<PaymentResponse> paymentResponseList = paymentResponses.paymentResponses().stream().filter(item->item.paymentStatus() == FAILED).toList();
		assertEquals(expectedData.size(), paymentResponseList.size(), "결제 내역의 개수가 일치해야 합니다.");

		for (int i = 0; i < expectedData.size(); i++) {
			Map<String, String> expected = expectedData.get(i);
			PaymentResponse actual = paymentResponseList.get(i);

			boolean isMatch = ResponseMatcher.matchResponse(expected, actual);
			assertTrue(isMatch, "결제 내역이 일치해야 합니다.");
		}
	}

	private void retrievePaymentHistoryForNonExistentUser(Long userId, int statusCode) {
		ExtractableResponse<Response> response = retrievePaymentHistory(userId);
		assertEquals(statusCode, response.statusCode(), "응답 상태 코드가 일치해야 합니다.");
		if (statusCode == 204) {
			putPaymentHistory(userId, new PaymentResponses(List.of()));
		}
	}

	private void verifyEmptyPaymentHistory() {
		PaymentResponses paymentResponses = getMostRecentPaymentHistory();
		assertNull(paymentResponses, "PaymentResponses should not null.");
	}
}
