package io.redisservice.api.infrastructure.repository;

import java.util.concurrent.TimeUnit;

import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import io.redisservice.api.business.dto.command.CacheCommand;
import io.redisservice.api.business.repository.RedisCacheRepository;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/29
 */
@Component
@RequiredArgsConstructor
public class RedisCacheCoreRepository implements RedisCacheRepository {

    private final RedissonClient redissonClient;

    @Override
    public boolean cache(CacheCommand cacheCommand) {
        RBucket<Object> bucket = redissonClient.getBucket(cacheCommand.getCacheKey());
        bucket.set(cacheCommand.getCacheValue(), cacheCommand.getTtl(), TimeUnit.SECONDS);
        return true;
    }

    @Override
    public Object getCache(String cacheKey) {
        RBucket<Object> bucket = redissonClient.getBucket(cacheKey);
        return bucket.get();
    }

    @Override
    public boolean evictCache(String cacheKey) {
        RBucket<Object> bucket = redissonClient.getBucket(cacheKey);
        return bucket.delete();
    }
}
