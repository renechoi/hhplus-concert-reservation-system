package io.paymentservice.api.common.cache.business.dto.info;

/**
 * @author : Rene Choi
 * @since : 2024/07/30
 */
public record EvictCacheInfo(
	 String cacheKey,
	 Boolean isEvicted
) {
}
