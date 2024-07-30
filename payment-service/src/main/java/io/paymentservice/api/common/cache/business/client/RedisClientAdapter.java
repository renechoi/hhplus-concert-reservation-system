package io.paymentservice.api.common.cache.business.client;

import java.util.Optional;

import io.paymentservice.api.common.cache.business.dto.command.CacheCommand;
import io.paymentservice.api.common.cache.business.dto.command.EvictCacheCommand;
import io.paymentservice.api.common.cache.business.dto.info.CacheInfo;
import io.paymentservice.api.common.cache.business.dto.info.EvictCacheInfo;

/**
 * @author : Rene Choi
 * @since : 2024/07/30
 */
public interface RedisClientAdapter {
	CacheInfo cache(CacheCommand command);

	Optional<CacheInfo> getCache(String cacheKey);

	EvictCacheInfo evictCache(EvictCacheCommand request);
}
