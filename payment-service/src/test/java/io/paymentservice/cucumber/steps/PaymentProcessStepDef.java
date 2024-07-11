package io.paymentservice.cucumber.steps;

import static io.paymentservice.api.payment.business.domainentity.PaymentStatus.*;
import static io.paymentservice.testhelpers.apiexecutor.PaymentApiExecutor.*;
import static io.paymentservice.testhelpers.contextholder.PaymentContextHolder.*;
import static io.paymentservice.testhelpers.parser.PaymentResponseParser.*;
import static io.paymentservice.util.FieldMapper.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Map;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java8.En;
import io.paymentservice.api.payment.business.persistence.PaymentTransactionRepository;
import io.paymentservice.api.payment.presentation.dto.request.PaymentRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

/**
 * @author : Rene Choi
 * @since : 2024/07/10
 */
public class PaymentProcessStepDef implements En {

	private PaymentTransactionRepository paymentTransactionRepository;
	public PaymentProcessStepDef(PaymentTransactionRepository paymentTransactionRepository

	) {
		this.paymentTransactionRepository = paymentTransactionRepository;


		Given("결제 요청이 다음과 같이 주어졌을 때", this::givenPaymentRequest);
		When("결제 요청을 처리하고 정상 응답을 받는다", this::processPaymentWithSuccessResponse);
		Then("결제 상태는 COMPLETE여야 한다", this::verifyPaymentStatusSuccess);
		Then("결제 내역이 저장되어야 한다", this::verifyPaymentSaved);

		When("결제 요청을 처리하면 실패 응답을 받는다", this::processPaymentWithFailureResponse);
		Then("결제 상태는 FAILED여야 한다", this::verifyPaymentStatusFailed);

		When("결제 요청을 처리하면 저장 중 실패 응답을 받는다", this::processPaymentWithSaveFailureResponse);
		Then("결제 내역이 저장되지 않아야 한다", this::verifyPaymentNotSaved);
	}


	private void givenPaymentRequest(DataTable dataTable) {
		Map<String, String> data = dataTable.asMaps().get(0);
		putPaymentRequest(updateFields(new PaymentRequest(), data));
	}

	private void processPaymentWithSuccessResponse() {
		putPaymentResponse(parsePaymentResponse(processPaymentWithOk(getMostRecentPaymentRequest())));
	}

	private void verifyPaymentStatusSuccess() {
		assertEquals(COMPLETE, getMostRecentPaymentResponse().paymentStatus(), "결제 상태가 COMPLETE여야 합니다.");
	}

	private void verifyPaymentSaved() {
		assertNotNull(getMostRecentPaymentResponse().transactionId(), "결제 내역이 저장되어야 합니다.");
	}

	private void processPaymentWithFailureResponse() {
		ExtractableResponse<Response> response = processPayment(getMostRecentPaymentRequest());
		assertNotEquals(200, response.statusCode());
		putPaymentResponse(parsePaymentResponseAsApiResponse(response));
	}

	private void verifyPaymentStatusFailed() {
		assertEquals(FAILED, getMostRecentPaymentResponse().paymentStatus(), "결제 상태가 FAILED여야 합니다.");
	}


	private void processPaymentWithSaveFailureResponse() {
		try {
			ExtractableResponse<Response> response = processPayment(getMostRecentPaymentRequest());
			assertNotEquals(200, response.statusCode());
		} catch (RuntimeException e) {
			assertEquals("Save operation failed", e.getMessage());
		}
	}

	private void verifyPaymentNotSaved() {
	}

	@Before("@mockPaymentTransactionRepository")
	public void beforeScenario() {
		doThrow(new RuntimeException("Save operation failed")).when(paymentTransactionRepository).save(any());
	}
}


