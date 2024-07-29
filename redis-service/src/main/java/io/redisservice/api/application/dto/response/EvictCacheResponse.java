package io.redisservice.api.application.dto.response;

import io.redisservice.api.business.dto.info.EvictCacheInfo;
import io.redisservice.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/07/30
 */
public record EvictCacheResponse (
	String cacheKey,
	Boolean isEvicted
) {

	public static EvictCacheResponse from(EvictCacheInfo evictCacheInfo) {
		return ObjectMapperBasedVoMapper.convert(evictCacheInfo,EvictCacheResponse.class);
	}

}
