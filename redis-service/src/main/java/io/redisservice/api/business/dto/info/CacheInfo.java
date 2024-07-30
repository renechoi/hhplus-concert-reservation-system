package io.redisservice.api.business.dto.info;

import io.redisservice.api.business.dto.command.CacheCommand;

/**
 * @author : Rene Choi
 * @since : 2024/07/29
 */
public record CacheInfo(
    String cacheKey,
    Object cacheValue,
    Long ttl,
    Boolean isCached
) {
    public static CacheInfo createCacheInfo(CacheCommand cacheCommand, boolean isCached) {
        return new CacheInfo(cacheCommand.getCacheKey(), cacheCommand.getCacheValue(), cacheCommand.getTtl(), isCached);
    }

    public static CacheInfo of(String cacheKey, String value) {
        return new CacheInfo(cacheKey, value, null, true);
    }
}
