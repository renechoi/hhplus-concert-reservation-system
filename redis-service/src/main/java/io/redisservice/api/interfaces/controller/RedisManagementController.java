package io.redisservice.api.interfaces.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.redisservice.api.application.facade.RedisCacheFacade;
import io.redisservice.api.application.facade.RedisManagementFacade;
import io.redisservice.common.model.CommonApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/31
 */
@RestController
@RequestMapping("/api/redis")
@RequiredArgsConstructor
public class RedisManagementController {

	private final RedisManagementFacade facade;

	@DeleteMapping("/clear")
	@Operation(summary = "Clear all Redis data")
	public CommonApiResponse<Void> clearAllData() {
		facade.clearAllData();
		return CommonApiResponse.OK();
	}
}
