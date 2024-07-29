package io.redisservice.testhelpers.parser;

import io.redisservice.api.application.dto.response.CacheResponse;
import io.redisservice.api.application.dto.response.EvictCacheResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
/**
 * @author : Rene Choi
 * @since : 2024/07/30
 */
public class RedisCacheResponseParser {
	public static CacheResponse parseCacheResponse(ExtractableResponse<Response> response) {
		return response.as(CacheResponse.class);
	}

	public static EvictCacheResponse parseEvictCacheResponse(ExtractableResponse<Response> response) {
		return response.as(EvictCacheResponse.class);
	}
}