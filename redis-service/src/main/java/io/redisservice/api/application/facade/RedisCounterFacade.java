package io.redisservice.api.application.facade;

import org.springframework.stereotype.Component;

import io.redisservice.api.application.dto.request.CounterRequest;
import io.redisservice.api.application.dto.response.CounterResponse;
import io.redisservice.api.business.service.RedisCounterService;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/31
 */
@Component
@RequiredArgsConstructor
public class RedisCounterFacade {

	private final RedisCounterService redisCounterService;

	public CounterResponse increment(CounterRequest request) {
		return CounterResponse.from(redisCounterService.increment(request.toCommand()));
	}

	public CounterResponse decrement(CounterRequest request) {
		return CounterResponse.from(redisCounterService.decrement(request.toCommand()));
	}

	public CounterResponse getCounter(String counterKey) {
		return CounterResponse.from(redisCounterService.getCounter(counterKey));
	}
}