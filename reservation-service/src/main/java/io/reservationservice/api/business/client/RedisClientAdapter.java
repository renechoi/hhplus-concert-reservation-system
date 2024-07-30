package io.reservationservice.api.business.client;

import java.util.Optional;

import io.reservationservice.api.business.dto.inport.CacheCommand;
import io.reservationservice.api.business.dto.inport.EvictCacheCommand;
import io.reservationservice.api.business.dto.outport.CacheInfo;
import io.reservationservice.api.business.dto.outport.EvictCacheInfo;

/**
 * @author : Rene Choi
 * @since : 2024/07/30
 */
public interface RedisClientAdapter {
	CacheInfo cache(CacheCommand command);

	Optional<CacheInfo> getCache(String cacheKey);

	EvictCacheInfo evictCache(EvictCacheCommand request);
}
