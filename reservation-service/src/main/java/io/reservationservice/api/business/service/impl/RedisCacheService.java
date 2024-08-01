package io.reservationservice.api.business.service.impl;

import org.springframework.stereotype.Service;

import io.reservationservice.api.business.client.RedisClientAdapter;
import io.reservationservice.api.business.dto.inport.CacheCommand;
import io.reservationservice.api.business.dto.inport.EvictCacheCommand;
import io.reservationservice.api.business.dto.outport.CacheInfo;
import io.reservationservice.api.business.dto.outport.EvictCacheInfo;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/30
 */
@Service
@RequiredArgsConstructor
public class RedisCacheService {
	private final RedisClientAdapter redisClientAdapter;

	public CacheInfo cache(CacheCommand command) {
		return redisClientAdapter.cache(command).confirm();
	}

	public CacheInfo get(String cacheKey) {
		return redisClientAdapter.getCache(cacheKey).orElse(null);
	}

	public EvictCacheInfo evict(EvictCacheCommand command) {
		return redisClientAdapter.evictCache(command);
	}
}
