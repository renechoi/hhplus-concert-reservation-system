package io.redisservice.api.business.service;

import org.springframework.stereotype.Service;

import io.redisservice.api.business.dto.command.CacheCommand;
import io.redisservice.api.business.dto.info.CacheInfo;
import io.redisservice.api.business.dto.info.EvictCacheInfo;
import io.redisservice.api.business.repository.RedisCacheRepository;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/29
 */
@Service
@RequiredArgsConstructor
public class RedisCacheService {

	private final RedisCacheRepository redisCacheRepository;

	public CacheInfo cache(CacheCommand cacheCommand) {
		boolean cached = redisCacheRepository.cache(cacheCommand);
		return CacheInfo.createCacheInfo(cacheCommand, cached);
	}

	public Object getCache(String cacheKey) {
		return redisCacheRepository.getCache(cacheKey);
	}

	public EvictCacheInfo evictCache(String cacheKey) {
		boolean evicted = redisCacheRepository.evictCache(cacheKey);
		return EvictCacheInfo.createEvictCacheInfo(cacheKey, evicted);
	}
}