package io.redisservice.api.interfaces.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.redisservice.api.application.dto.LockRequest;
import io.redisservice.api.application.dto.LockResponse;
import io.redisservice.api.application.dto.UnLockRequest;
import io.redisservice.api.application.dto.UnLockResponse;
import io.redisservice.api.application.facade.RedisLockFacade;
import io.redisservice.common.model.CommonApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/23
 */
@RestController
@RequestMapping("/api/redis-lock")
@RequiredArgsConstructor
public class RedisLockController {
	private final RedisLockFacade redisLockFacade;

	@PostMapping("/lock")
	@Operation(summary = "Redis Lock API")
	public CommonApiResponse<LockResponse> lock(@RequestBody @Validated LockRequest request) {
		return CommonApiResponse.OK(redisLockFacade.lock(request));
	}

	@PostMapping("/unlock")
	@Operation(summary = "Redis Unlock API")
	public CommonApiResponse<UnLockResponse> unlock(@RequestBody @Validated UnLockRequest request) {
		return CommonApiResponse.OK(redisLockFacade.unlock(request));
	}
}