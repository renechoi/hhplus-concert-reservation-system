package io.redisservice.api.infrastructure.repository;

import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import io.redisservice.api.business.repository.RedisCounterRepository;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/31
 */
@Component
@RequiredArgsConstructor
public class RedisCounterCoreRepository implements RedisCounterRepository {

	private final RedissonClient redissonClient;

	@Override
	public long increment(String counterKey, long value) {
		RAtomicLong counter = redissonClient.getAtomicLong(counterKey);
		return counter.addAndGet(value);
	}

	@Override
	public long decrement(String counterKey, long value) {
		RAtomicLong counter = redissonClient.getAtomicLong(counterKey);
		return counter.addAndGet(-value);
	}

	@Override
	public long getCounter(String counterKey) {
		RAtomicLong counter = redissonClient.getAtomicLong(counterKey);
		return counter.get();
	}
}
