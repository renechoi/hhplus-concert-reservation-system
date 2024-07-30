package io.reservationservice.api.business.dto.outport;

/**
 * @author : Rene Choi
 * @since : 2024/07/30
 */
public record EvictCacheInfo(
	 String cacheKey,
	 Boolean isEvicted
) {
}
