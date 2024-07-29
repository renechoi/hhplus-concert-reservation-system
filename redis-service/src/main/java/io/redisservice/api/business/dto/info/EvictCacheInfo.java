package io.redisservice.api.business.dto.info;

/**
 * @author : Rene Choi
 * @since : 2024/07/30
 */
public record EvictCacheInfo(
	String cacheKey,
	Boolean isEvicted
) {
	public static EvictCacheInfo createEvictCacheInfo(String cacheKey, boolean isEvicted) {
		return new EvictCacheInfo(cacheKey, isEvicted);
	}
}