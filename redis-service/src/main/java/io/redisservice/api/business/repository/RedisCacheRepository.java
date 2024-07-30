package io.redisservice.api.business.repository;

import java.util.Optional;

import io.redisservice.api.business.dto.command.CacheCommand;

/**
 * @author : Rene Choi
 * @since : 2024/07/29
 */
public interface RedisCacheRepository {
	boolean cache(CacheCommand cacheCommand);
	String getCache(String cacheKey);
	boolean evictCache(String cacheKey);
}
