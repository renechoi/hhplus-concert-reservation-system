package io.paymentservice.testhelpers.apiexecutor;

import static io.paymentservice.testhelpers.apiexecutor.DynamicPortHolder.*;
import static io.restassured.RestAssured.*;
import static org.springframework.util.MimeTypeUtils.*;

import io.paymentservice.api.payment.presentation.dto.request.PaymentRequest;
import io.paymentservice.util.YmlLoader;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * @author : Rene Choi
 * @since : 2024/07/10
 */

public class PaymentApiExecutor extends AbstractRequestExecutor {

	private static final String CONTEXT_PATH = YmlLoader.ymlLoader().getContextPath();
	private static final String PAYMENT_API_URL_PATH = CONTEXT_PATH + "/api/user-balance";

	private static RequestSpecification getRequestSpecification(int port) {
		return given().log().all().port(port).contentType(APPLICATION_JSON_VALUE);
	}

	public static ExtractableResponse<Response> processPayment(PaymentRequest request) {
		return doPost(getRequestSpecification(getPort()), PAYMENT_API_URL_PATH + "/payment", request);
	}

	public static ExtractableResponse<Response> processPaymentWithOk(PaymentRequest request) {
		return doPostWithOk(getRequestSpecification(getPort()), PAYMENT_API_URL_PATH + "/payment", request);
	}

	public static ExtractableResponse<Response> retrievePaymentHistory(Long userId) {
		return doGet(getRequestSpecification(getPort()), PAYMENT_API_URL_PATH + "/payment/history/" + userId);
	}

	public static ExtractableResponse<Response> retrievePaymentHistoryWithOk(Long userId) {
		return doGetWithOk(getRequestSpecification(getPort()), PAYMENT_API_URL_PATH + "/payment/history/" + userId);
	}



}