package io.paymentservice.api.common.cache.business.service;

import org.springframework.stereotype.Service;

import io.paymentservice.api.common.cache.business.client.RedisClientAdapter;
import io.paymentservice.api.common.cache.business.dto.command.CacheCommand;
import io.paymentservice.api.common.cache.business.dto.command.EvictCacheCommand;
import io.paymentservice.api.common.cache.business.dto.info.CacheInfo;
import io.paymentservice.api.common.cache.business.dto.info.EvictCacheInfo;
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
