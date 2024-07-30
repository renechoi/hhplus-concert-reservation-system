package io.redisservice.api.business.service;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import io.redisservice.api.business.dto.command.CacheCommand;
import io.redisservice.api.business.dto.info.CacheInfo;
import io.redisservice.api.business.dto.info.EvictCacheInfo;
import io.redisservice.api.business.repository.RedisCacheRepository;
import io.redisservice.common.exception.definitions.CacheValueNotFoundException;
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

	public CacheInfo  getCache(String cacheKey) {
		return CacheInfo.of(cacheKey,redisCacheRepository.getCache(cacheKey));
	}

	public EvictCacheInfo evictCache(String cacheKey) {
		boolean evicted = redisCacheRepository.evictCache(cacheKey);
		return EvictCacheInfo.createEvictCacheInfo(cacheKey, evicted);
	}
}