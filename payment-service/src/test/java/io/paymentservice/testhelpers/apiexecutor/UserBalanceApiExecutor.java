package io.paymentservice.testhelpers.apiexecutor;

import static io.paymentservice.testhelpers.apiexecutor.DynamicPortHolder.*;
import static io.restassured.RestAssured.*;
import static org.springframework.http.MediaType.*;

import io.paymentservice.api.balance.presentation.dto.request.UserBalanceChargeRequest;
import io.paymentservice.api.balance.presentation.dto.request.UserBalanceUseRequest;
import io.paymentservice.util.YmlLoader;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * @author : Rene Choi
 * @since : 2024/07/09
 */

public class UserBalanceApiExecutor extends AbstractRequestExecutor {

	private static final String CONTEXT_PATH = YmlLoader.ymlLoader().getContextPath();

	private static final String BALANCE_API_URL_PATH = CONTEXT_PATH + "/api/user-balance";

	private static RequestSpecification getRequestSpecification(int port) {
		return given().log().all().port(port).contentType(APPLICATION_JSON_VALUE);
	}

	public static ExtractableResponse<Response> getUserBalance(long userId) {
		return doGet(getRequestSpecification(getPort()), BALANCE_API_URL_PATH + "/" + userId);
	}

	public static ExtractableResponse<Response> getUserBalanceHistories(long userId) {
		return doGet(getRequestSpecification(getPort()), BALANCE_API_URL_PATH + "/histories/" + userId);
	}

	public static ExtractableResponse<Response> chargeUserBalance(long userId, UserBalanceChargeRequest request) {
		return doPut(getRequestSpecification(getPort()), BALANCE_API_URL_PATH + "/charge/" + userId, request);
	}

	public static ExtractableResponse<Response> chargeUserBalanceWithOk(long userId, UserBalanceChargeRequest request) {
		return doPutWithOk(getRequestSpecification(getPort()), BALANCE_API_URL_PATH + "/charge/" + userId, request);
	}
	public static ExtractableResponse<Response> useUserBalance(long userId, UserBalanceUseRequest request) {
		return doPut(getRequestSpecification(getPort()), BALANCE_API_URL_PATH + "/use/" + userId, request);
	}

	public static ExtractableResponse<Response> useUserBalanceWithOk(long userId, UserBalanceUseRequest request) {
		return doPutWithOk(getRequestSpecification(getPort()), BALANCE_API_URL_PATH + "/use/" + userId, request);
	}
}
