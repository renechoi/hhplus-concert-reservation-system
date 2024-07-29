package io.redisservice.api.application.facade;

import org.springframework.stereotype.Component;

import io.redisservice.api.application.dto.request.CacheRequest;
import io.redisservice.api.application.dto.response.CacheResponse;
import io.redisservice.api.application.dto.response.EvictCacheResponse;
import io.redisservice.api.business.service.RedisCacheService;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/29
 */
@Component
@RequiredArgsConstructor
public class RedisCacheFacade {

	private final RedisCacheService redisCacheService;

	public CacheResponse cache(CacheRequest request) {
		return CacheResponse.from(redisCacheService.cache(request.toCommand()));
	}

	public Object getCache(String cacheKey) {
		return redisCacheService.getCache(cacheKey);
	}

	public EvictCacheResponse evictCache(String cacheKey) {
		return EvictCacheResponse.from(redisCacheService.evictCache(cacheKey));
	}
}