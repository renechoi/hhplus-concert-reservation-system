package io.redisservice.cucumber.steps;

import static io.redisservice.testhelpers.apiexecutor.RedisCacheApiExecutor.*;
import static io.redisservice.testhelpers.contextholder.RedisCacheContextHolder.*;
import static io.redisservice.testhelpers.fieldmatcher.ResponseMatcher.*;
import static io.redisservice.testhelpers.parser.RedisCacheResponseParser.*;
import static io.redisservice.util.FieldMapper.*;
import static org.junit.jupiter.api.Assertions.*;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.redisservice.api.application.dto.request.CacheRequest;
import io.redisservice.api.application.dto.request.EvictCacheRequest;
import io.redisservice.api.application.dto.response.CacheResponse;
import io.redisservice.api.application.dto.response.EvictCacheResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

/**
 * @author : Rene Choi
 * @since : 2024/07/30
 */
public class RedisCacheApiStepDef implements En {

	public RedisCacheApiStepDef() {

		Given("다음과 같은 Redis 캐시 요청을 보내고 성공 응답을 받는다", this::sendCacheRequest);
		Then("Redis 캐시 응답은 다음과 같이 확인되어야 한다", this::verifyCacheResponse);

		When("다음과 같은 Redis 캐시 조회 요청을 보낸다", this::sendGetCacheRequest);
		Then("Redis 캐시 조회 응답은 다음과 같이 확인되어야 한다", this::verifyGetCacheResponse);

		When("다음과 같은 Redis 캐시 삭제 요청을 보낸다", this::sendEvictCacheRequest);
		Then("Redis 캐시 삭제 응답은 다음과 같이 확인되어야 한다", this::verifyEvictCacheResponse);

		Then("Redis 캐시 조회 응답은 없음을 확인한다", this::verifyCacheIsAbsent);

	}

	private void sendCacheRequest(DataTable dataTable) {
		dataTable.asMaps().forEach(row -> {
			CacheRequest request = updateFields(new CacheRequest(), row);
			CacheResponse cacheResponse = parseCacheResponse(cache(request));

			putCacheRequest(request.getCacheKey(), request);
			putCacheResponse(request.getCacheKey(), cacheResponse);
		});
	}

	private void sendGetCacheRequest(DataTable dataTable) {
		dataTable.asMaps().forEach(row -> {
			String cacheKey = row.get("cacheKey");
			ExtractableResponse<Response> response = getCache(cacheKey);

			String cacheValue = response.asString();

			try {
				putCacheResponse(cacheKey, new ObjectMapper().readValue(cacheValue, CacheResponse.class));
			} catch (Exception ignored) {
			}
			putCacheRequest(cacheKey, getCacheRequest(cacheKey));
		});
	}

	private void sendEvictCacheRequest(DataTable dataTable) {
		dataTable.asMaps().forEach(row -> {
			String cacheKey = row.get("cacheKey");
			EvictCacheRequest request = EvictCacheRequest.builder().cacheKey(cacheKey).build();
			EvictCacheResponse evictCacheResponse = parseEvictCacheResponse(evictCache(request));

			putCacheRequest(cacheKey, getCacheRequest(cacheKey));
			putEvictCacheResponse(cacheKey, evictCacheResponse);
		});
	}

	private void verifyCacheResponse(DataTable dataTable) {
		dataTable.asMaps().forEach(row -> assertTrue(matchResponse(row, getCacheResponse(row.get("cacheKey")))));
	}

	private void verifyGetCacheResponse(DataTable dataTable) {
		dataTable.asMaps().forEach(row -> {
			String cacheKey = row.get("cacheKey");
			CacheResponse expectedResponse = getCacheResponse(cacheKey);
			String expectedValue = row.get("cacheValue");

			// 파싱하여 JSON 동등성 비교
			assertEquals(cacheKey, expectedResponse.getCacheKey());
			assertEquals("\"" + expectedValue + "\"", expectedResponse.getCacheValue());
		});
	}

	private void verifyEvictCacheResponse(DataTable dataTable) {
		dataTable.asMaps().forEach(row -> assertTrue(matchResponse(row, getCacheResponse(row.get("cacheKey")))));
	}

	private void verifyCacheIsAbsent(DataTable dataTable) {
		dataTable.asMaps().forEach(row -> {
			String cacheKey = row.get("cacheKey");

			ExtractableResponse<Response> cacheResponse = getCache(cacheKey);
			assertEquals(204, cacheResponse.statusCode(), "캐시가 없어야 함 -> key: " + cacheKey);
		});
	}

}
