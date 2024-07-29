package io.redisservice.testhelpers.apiexecutor;

import static io.redisservice.testhelpers.apiexecutor.DynamicPortHolder.*;
import static io.restassured.RestAssured.*;
import static org.springframework.http.MediaType.*;

import io.redisservice.api.application.dto.request.CacheRequest;
import io.redisservice.util.YmlLoader;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * @author : Rene Choi
 * @since : 2024/07/30
 */
public class RedisCacheApiExecutor extends AbstractRequestExecutor {

	private static final String CONTEXT_PATH = YmlLoader.ymlLoader().getContextPath();

	private static final String REDIS_SERVICE_API_URL_PATH = CONTEXT_PATH + "/api/redis-cache";
	private static final String CACHE_API_URL_PATH = REDIS_SERVICE_API_URL_PATH + "/cache";

	private static RequestSpecification getRequestSpecification(int port) {
		return given().log().all().port(port).contentType(APPLICATION_JSON_VALUE);
	}

	public static ExtractableResponse<Response> cache(CacheRequest request) {
		return doPost(getRequestSpecification(getPort()), CACHE_API_URL_PATH, request);
	}

	public static ExtractableResponse<Response> getCache(String cacheKey) {
		return doGet(getRequestSpecification(getPort()), CACHE_API_URL_PATH + "/" + cacheKey);
	}

	public static ExtractableResponse<Response> evictCache(String cacheKey) {
		return doDelete(getRequestSpecification(getPort()), CACHE_API_URL_PATH + "/" + cacheKey);
	}
}
