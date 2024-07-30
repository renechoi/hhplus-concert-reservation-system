package io.redisservice.api.infrastructure.repository;

import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import io.redisservice.api.business.repository.RedisManagementRepository;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/31
 */
@Component
@RequiredArgsConstructor
public class RedisManagementCoreRepository implements RedisManagementRepository {
	private final RedissonClient redissonClient;

	@Override
	public void clearAllData() {
		redissonClient.getKeys().flushall();
	}
}
