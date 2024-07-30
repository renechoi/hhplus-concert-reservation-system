package io.reservationservice.api.business.dto.outport;

/**
 * @author : Rene Choi
 * @since : 2024/07/30
 */
public record CacheInfo(
	 String cacheKey,
	 Object cacheValue,
	 Long ttl,
	 Boolean isCached
) {
	public CacheInfo confirm() {
		return new CacheInfo(cacheKey, cacheValue, ttl, true);
	}
}
