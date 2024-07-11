package io.clientchannelservice.testhelpers.apiexecutor;

import static io.clientchannelservice.testhelpers.apiexecutor.DynamicPortHolder.*;
import static io.restassured.RestAssured.*;
import static org.springframework.util.MimeTypeUtils.*;

import io.clientchannelservice.util.YmlLoader;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
public class WaitingQueueTokenPollingApiExecutor extends AbstractRequestExecutor {
	private static final String CONTEXT_PATH = YmlLoader.ymlLoader().getContextPath();
	private static final String POLLING_API_URL_PATH = CONTEXT_PATH + "/api/polling";

	private static RequestSpecification getRequestSpecification(int port) {
		return given().log().all().port(port).contentType(APPLICATION_JSON_VALUE);
	}

	public static ExtractableResponse<Response> retrieveWaitingQueueToken(String userId) {
		return doGet(getRequestSpecification(getPort()), POLLING_API_URL_PATH + "/" + userId);
	}
}