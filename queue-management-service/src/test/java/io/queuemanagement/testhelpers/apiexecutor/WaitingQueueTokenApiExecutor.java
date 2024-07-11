package io.queuemanagement.testhelpers.apiexecutor;

import static io.queuemanagement.testhelpers.apiexecutor.DynamicPortHolder.*;
import static io.restassured.RestAssured.*;
import static org.springframework.http.MediaType.*;

import io.queuemanagement.api.application.dto.request.WaitingQueueTokenGenerateRequest;
import io.queuemanagement.util.YmlLoader;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * @author : Rene Choi
 * @since : 2024/07/06
 */

public class WaitingQueueTokenApiExecutor extends AbstractRequestExecutor{
	private static final String CONTEXT_PATH = YmlLoader.ymlLoader().getContextPath();
	private static final String TOKEN_API_URL_PATH = CONTEXT_PATH + "/api/waiting-queue-token";


	private static RequestSpecification getRequestSpecification(int port) {
		return given().log().all().port(port).contentType(APPLICATION_JSON_VALUE);
	}

	public static ExtractableResponse<Response> generateWaitingQueueToken(WaitingQueueTokenGenerateRequest request) {
		return doPost(getRequestSpecification(getPort()), TOKEN_API_URL_PATH, request);
	}


	public static ExtractableResponse<Response> retrieveWaitingQueueToken(String  userId) {
		return doGet(getRequestSpecification(getPort()), TOKEN_API_URL_PATH + "/" + userId);
	}
}