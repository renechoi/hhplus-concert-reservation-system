package io.paymentservice.api.payment.interfaces.controller;

import static io.paymentservice.api.payment.business.entity.PaymentStatus.*;
import static io.paymentservice.testhelpers.apiexecutor.PaymentApiExecutor.*;
import static io.paymentservice.testhelpers.apiexecutor.BalanceApiExecutor.*;
import static io.paymentservice.testhelpers.contextholder.PaymentContextHolder.*;
import static io.paymentservice.testhelpers.contextholder.BalanceContextHolder.*;
import static io.paymentservice.testhelpers.parser.PaymentResponseParser.*;
import static io.paymentservice.testhelpers.parser.BalanceResponseParser.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import io.paymentservice.api.balance.interfaces.dto.request.BalanceChargeRequest;
import io.paymentservice.api.payment.CommonAcceptanceTest;
import io.paymentservice.api.payment.business.entity.PaymentMethod;
import io.paymentservice.api.payment.business.entity.PaymentTransaction;
import io.paymentservice.api.payment.business.persistence.PaymentTransactionRepository;
import io.paymentservice.api.payment.interfaces.dto.request.PaymentRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

/**
 * @author : Rene Choi
 * @since : 2024/07/10
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("결제 중 실패 - 롤백 시나리오")
class PaymentRollbackAcceptanceTest extends CommonAcceptanceTest {

	@SpyBean
	private PaymentTransactionRepository spyPaymentTransactionRepository;

	@BeforeEach
	public void setup() {
		Mockito.doThrow(new RuntimeException("Database error"))
			.when(spyPaymentTransactionRepository).save(any(PaymentTransaction.class));
	}

	@AfterEach
	public void teardown() {
		Mockito.reset(spyPaymentTransactionRepository);
	}

	@Test
	void 결제_저장_중_실패하여_롤백되는_시나리오() throws Exception {
		// Given
		putPaymentRequest(new PaymentRequest(1L, BigDecimal.valueOf(1000), PaymentMethod.CREDIT_CARD));
		putChargeResponse(1L, parseBalanceChargeResponse(chargeBalanceWithOk(1L, new BalanceChargeRequest(1L, BigDecimal.valueOf(3000)))));

		// When
		ExtractableResponse<Response> response = processPayment(getMostRecentPaymentRequest());
		assertNotEquals(200, response.statusCode());
		assertEquals(FAILED, parsePaymentResponseAsApiResponse(response).paymentStatus());

		ExtractableResponse<Response> getBalanceResponse = getBalance(1L);
		assertEquals(200, getBalanceResponse.statusCode());
		assertEquals(3000, parseBalanceSearchResponse(getBalanceResponse).amount().longValue(), "조회한 잔액이 예상 값과 다릅니다.");

	}

}