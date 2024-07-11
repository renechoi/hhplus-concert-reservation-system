package io.apiorchestrationservice.testhelpers.apiexecutor;

import static io.apiorchestrationservice.testhelpers.apiexecutor.DynamicPortHolder.*;
import static io.restassured.RestAssured.*;
import static org.springframework.util.MimeTypeUtils.*;

import io.apiorchestrationservice.api.application.dto.request.PaymentProcessRequest;
import io.apiorchestrationservice.api.application.dto.request.UserBalanceChargeRequest;
import io.apiorchestrationservice.util.YmlLoader;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * @author : Rene Choi
 * @since : 2024/07/10
 */

public class BalanceAndPaymentApiExecutor extends AbstractRequestExecutor {

	private static final String CONTEXT_PATH = YmlLoader.ymlLoader().getContextPath();
	private static final String BALANCE_API_URL_PATH = CONTEXT_PATH + "/api/user-balance";

	private static RequestSpecification getRequestSpecification(int port) {
		return given().log().all().port(port).contentType(APPLICATION_JSON_VALUE);
	}

	public static ExtractableResponse<Response> chargeUserBalance(UserBalanceChargeRequest request) {
		return doPost(getRequestSpecification(getPort()), BALANCE_API_URL_PATH + "/charge", request);
	}

	public static ExtractableResponse<Response> chargeUserBalanceWithOk(UserBalanceChargeRequest request) {
		return doPostWithOk(getRequestSpecification(getPort()), BALANCE_API_URL_PATH + "/charge", request);
	}

	public static ExtractableResponse<Response> getUserBalance(Long userId) {
		return doGet(getRequestSpecification(getPort()), BALANCE_API_URL_PATH + "/" + userId);
	}

	public static ExtractableResponse<Response> getUserBalanceWithOk(Long userId) {
		return doGet(getRequestSpecification(getPort()), BALANCE_API_URL_PATH + "/" + userId);
	}

	public static ExtractableResponse<Response> processPayment(PaymentProcessRequest request) {
		return doPost(getRequestSpecification(getPort()), BALANCE_API_URL_PATH + "/payment", request);
	}

	public static ExtractableResponse<Response> processPaymentWithToken(PaymentProcessRequest request, String token) {
		return doPostWithToken(getRequestSpecification(getPort()), BALANCE_API_URL_PATH + "/payment", request, token);
	}
}
