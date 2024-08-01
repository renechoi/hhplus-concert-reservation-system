package io.redisservice.api.business.service;

import org.springframework.stereotype.Service;

import io.redisservice.api.business.repository.RedisManagementRepository;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/31
 */
@Service
@RequiredArgsConstructor
public class RedisManagementService {
	private final RedisManagementRepository redisManagementRepository;

	public void clearAllData() {
		redisManagementRepository.clearAllData();
	}
}
