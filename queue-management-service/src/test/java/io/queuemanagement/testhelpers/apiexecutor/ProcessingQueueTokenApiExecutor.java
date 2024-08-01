package io.queuemanagement.testhelpers.apiexecutor;

import static io.queuemanagement.testhelpers.apiexecutor.DynamicPortHolder.*;
import static io.restassured.RestAssured.*;
import static org.springframework.http.MediaType.*;

import io.queuemanagement.util.YmlLoader;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
public class ProcessingQueueTokenApiExecutor extends AbstractRequestExecutor {
	private static final String CONTEXT_PATH = YmlLoader.ymlLoader().getContextPath();
	private static final String TOKEN_API_URL_PATH = CONTEXT_PATH + "/api/processing-queue-token";

	private static RequestSpecification getRequestSpecification(int port) {
		return given().log().all().port(port).contentType(APPLICATION_JSON_VALUE);
	}

	public static ExtractableResponse<Response> checkProcessingQueueTokenAvailability(String tokenValue, String userId) {
		return given()
			.spec(getRequestSpecification(getPort()))
			.header("X-Queue-Token", tokenValue)
			.get(TOKEN_API_URL_PATH + "/check-availability/" + userId)
			.then()
			.extract();
	}

	public static ExtractableResponse<Response> checkProcessingQueueTokenAvailability(String userId) {
		return given()
			.spec(getRequestSpecification(getPort()))
			.get(TOKEN_API_URL_PATH + "/check-availability/" + userId)
			.then()
			.extract();
	}
}
