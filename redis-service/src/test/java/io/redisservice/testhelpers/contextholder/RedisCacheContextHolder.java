package io.redisservice.testhelpers.contextholder;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import io.redisservice.api.application.dto.request.CacheRequest;
import io.redisservice.api.application.dto.response.CacheResponse;
import io.redisservice.api.application.dto.response.EvictCacheResponse;

/**
 * @author : Rene Choi
 * @since : 2024/07/30
 */
public class RedisCacheContextHolder implements TestDtoContextHolder {
	private static final ConcurrentHashMap<String, CacheResponse> cacheResponseMap = new ConcurrentHashMap<>();
	private static final ConcurrentHashMap<String, CacheRequest> cacheRequestMap = new ConcurrentHashMap<>();
	private static final ConcurrentHashMap<String, EvictCacheResponse> evictCacheResponseMap = new ConcurrentHashMap<>();
	private static final AtomicReference<String> mostRecentCacheKey = new AtomicReference<>();

	public static void initFields() {
		cacheResponseMap.clear();
		cacheRequestMap.clear();
		evictCacheResponseMap.clear();
		mostRecentCacheKey.set(null);
	}

	public static void putCacheRequest(String cacheKey, CacheRequest request) {
		cacheRequestMap.put(cacheKey, request);
		mostRecentCacheKey.set(cacheKey);
	}

	public static CacheRequest getCacheRequest(String cacheKey) {
		return cacheRequestMap.get(cacheKey);
	}

	public static void putCacheResponse(String cacheKey, CacheResponse response) {
		cacheResponseMap.put(cacheKey, response);
		mostRecentCacheKey.set(cacheKey);
	}

	public static CacheResponse getCacheResponse(String cacheKey) {
		return cacheResponseMap.get(cacheKey);
	}

	public static void putEvictCacheResponse(String cacheKey, EvictCacheResponse response) {
		evictCacheResponseMap.put(cacheKey, response);
		mostRecentCacheKey.set(cacheKey);
	}

	public static EvictCacheResponse getEvictCacheResponse(String cacheKey) {
		return evictCacheResponseMap.get(cacheKey);
	}

	public static String getMostRecentCacheKey() {
		return mostRecentCacheKey.get();
	}

	public static CacheResponse getMostRecentCacheResponse() {
		String recentCacheKey = mostRecentCacheKey.get();
		return recentCacheKey != null ? getCacheResponse(recentCacheKey) : null;
	}

	public static EvictCacheResponse getMostRecentEvictCacheResponse() {
		String recentCacheKey = mostRecentCacheKey.get();
		return recentCacheKey != null ? getEvictCacheResponse(recentCacheKey) : null;
	}
}