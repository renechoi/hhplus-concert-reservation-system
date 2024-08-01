package io.redisservice.api.infrastructure.repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.redisservice.api.business.dto.command.CacheCommand;
import io.redisservice.api.business.dto.command.EvictCacheCommand;
import io.redisservice.api.business.repository.RedisCacheRepository;
import io.redisservice.common.exception.definitions.CacheValueNotFoundException;
import io.redisservice.common.exception.definitions.CacheValueNotRetrievableException;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/29
 */
@Component
@RequiredArgsConstructor
public class RedisCacheCoreRepository implements RedisCacheRepository {

    private final RedissonClient redissonClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean cache(CacheCommand cacheCommand) {
        RBucket<Object> bucket = redissonClient.getBucket(cacheCommand.getCacheKey());
        bucket.set(cacheCommand.getCacheValue(), cacheCommand.getTtl(), TimeUnit.SECONDS);
        return true;
    }

    @Override
    public String getCache(String cacheKey) {
        Object value = redissonClient.getBucket(cacheKey).get();

        if (value == null) {
            throw new CacheValueNotFoundException();
        }
        try {
            return objectMapper.writeValueAsString(value);
        } catch (Exception e) {
            throw new CacheValueNotRetrievableException();
        }
    }

    @Override
    public boolean evictCache(EvictCacheCommand evictCacheCommand) {
        RBucket<Object> bucket = redissonClient.getBucket(evictCacheCommand.getCacheKey());
        return bucket.delete();
    }
}
