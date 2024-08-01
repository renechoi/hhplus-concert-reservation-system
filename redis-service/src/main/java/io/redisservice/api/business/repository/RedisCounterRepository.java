package io.redisservice.api.business.repository;

/**
 * @author : Rene Choi
 * @since : 2024/07/31
 */
public interface RedisCounterRepository {
	long increment(String counterKey, long value);

	long decrement(String counterKey, long value);

	long getCounter(String counterKey);
}
