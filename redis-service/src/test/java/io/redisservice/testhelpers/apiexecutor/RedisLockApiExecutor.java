package io.redisservice.testhelpers.apiexecutor;

import static io.redisservice.testhelpers.apiexecutor.DynamicPortHolder.*;
import static io.restassured.RestAssured.*;
import static org.springframework.http.MediaType.*;

import io.redisservice.api.application.dto.LockRequest;
import io.redisservice.api.application.dto.UnLockRequest;
import io.redisservice.util.YmlLoader;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * @author : Rene Choi
 * @since : 2024/07/23
 */

public class RedisLockApiExecutor extends AbstractRequestExecutor {

	private static final String CONTEXT_PATH = YmlLoader.ymlLoader().getContextPath();

	private static final String REDIS_SERVICE_API_URL_PATH = CONTEXT_PATH + "/api/redis-lock";
	private static final String LOCK_API_URL_PATH = REDIS_SERVICE_API_URL_PATH + "/lock";
	private static final String UNLOCK_API_URL_PATH = REDIS_SERVICE_API_URL_PATH + "/unlock";

	private static RequestSpecification getRequestSpecification(int port) {
		return given().log().all().port(port).contentType(APPLICATION_JSON_VALUE);
	}

	public static ExtractableResponse<Response> lock(LockRequest request) {
		return doPost(getRequestSpecification(getPort()), LOCK_API_URL_PATH, request);
	}

	public static ExtractableResponse<Response> lockWithOk(LockRequest request) {
		return doPostWithOk(getRequestSpecification(getPort()), LOCK_API_URL_PATH, request);
	}

	public static ExtractableResponse<Response> unlockWithOk(UnLockRequest request) {
		return doPostWithOk(getRequestSpecification(getPort()), UNLOCK_API_URL_PATH, request);
	}
}