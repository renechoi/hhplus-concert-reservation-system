package io.redisservice.api.application.facade;

import org.springframework.stereotype.Component;

import io.redisservice.api.business.service.RedisManagementService;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/31
 */
@Component
@RequiredArgsConstructor
public class RedisManagementFacade {
	private final RedisManagementService redisManagementService;

	public void clearAllData() {
		redisManagementService.clearAllData();
	}
}
