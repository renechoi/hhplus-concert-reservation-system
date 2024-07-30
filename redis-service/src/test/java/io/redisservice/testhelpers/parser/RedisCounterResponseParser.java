package io.redisservice.testhelpers.parser;

import io.redisservice.api.application.dto.response.CounterResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

/**
 * @author : Rene Choi
 * @since : 2024/07/31
 */
public class RedisCounterResponseParser {
    public static CounterResponse parseCounterResponse(ExtractableResponse<Response> response) {
        return response.as(CounterResponse.class);
    }
}
