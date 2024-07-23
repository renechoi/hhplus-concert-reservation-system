package io.redisservice.api.application.facade;

import org.springframework.stereotype.Component;

import io.redisservice.api.application.dto.LockRequest;
import io.redisservice.api.application.dto.LockResponse;
import io.redisservice.api.application.dto.UnLockRequest;
import io.redisservice.api.application.dto.UnLockResponse;
import io.redisservice.api.business.service.RedisLockService;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/23
 */
@Component
@RequiredArgsConstructor
public class RedisLockFacade {
	private final RedisLockService redisLockService;

	public LockResponse lock(LockRequest request) {
		return LockResponse.from(redisLockService.lock(request.toCommand()));
	}

	public UnLockResponse unlock(UnLockRequest request) {
		return UnLockResponse.from(redisLockService.unlock(request.toCommand()));
	}
}