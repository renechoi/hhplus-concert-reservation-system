package io.redisservice.api.business.service;

import org.springframework.stereotype.Service;

import io.redisservice.api.business.dto.command.CounterCommand;
import io.redisservice.api.business.dto.info.CounterInfo;
import io.redisservice.api.business.repository.RedisCounterRepository;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/31
 */
@Service
@RequiredArgsConstructor
public class RedisCounterService {

	private final RedisCounterRepository redisCounterRepository;

	public CounterInfo increment(CounterCommand command) {
		long newValue = redisCounterRepository.increment(command.getCounterKey(), command.getValue());
		return CounterInfo.of(command.getCounterKey(), newValue);
	}

	public CounterInfo decrement(CounterCommand command) {
		long newValue = redisCounterRepository.decrement(command.getCounterKey(), command.getValue());
		return CounterInfo.of(command.getCounterKey(), newValue);
	}

	public CounterInfo getCounter(String counterKey) {
		long value = redisCounterRepository.getCounter(counterKey);
		return CounterInfo.of(counterKey, value);
	}
}
