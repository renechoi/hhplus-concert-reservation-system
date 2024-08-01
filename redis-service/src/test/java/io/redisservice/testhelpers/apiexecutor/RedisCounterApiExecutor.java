package io.redisservice.testhelpers.apiexecutor;

import static io.redisservice.testhelpers.apiexecutor.DynamicPortHolder.*;
import static io.restassured.RestAssured.*;
import static org.springframework.http.MediaType.*;

import io.redisservice.api.application.dto.request.CounterRequest;
import io.redisservice.util.YmlLoader;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * @author : Rene Choi
 * @since : 2024/07/31
 */
public class RedisCounterApiExecutor extends AbstractRequestExecutor {

    private static final String CONTEXT_PATH = YmlLoader.ymlLoader().getContextPath();
    private static final String REDIS_SERVICE_API_URL_PATH = CONTEXT_PATH + "/api/redis-counter";

    private static RequestSpecification getRequestSpecification(int port) {
        return given().log().all().port(port).contentType(APPLICATION_JSON_VALUE);
    }

    public static ExtractableResponse<Response> increment(CounterRequest request) {
        return doPost(getRequestSpecification(getPort()), REDIS_SERVICE_API_URL_PATH + "/increment", request);
    }

    public static ExtractableResponse<Response> decrement(CounterRequest request) {
        return doPost(getRequestSpecification(getPort()), REDIS_SERVICE_API_URL_PATH + "/decrement", request);
    }

    public static ExtractableResponse<Response> getCounter(String counterKey) {
        return doGet(getRequestSpecification(getPort()), REDIS_SERVICE_API_URL_PATH + "/" + counterKey);
    }
}
