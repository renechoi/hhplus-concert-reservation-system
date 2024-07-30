package io.apiorchestrationservice.api.business.service.impl;

import org.springframework.stereotype.Service;

import io.apiorchestrationservice.api.business.client.RedisClientAdapter;
import io.apiorchestrationservice.api.business.dto.inport.DistributedLockCommand;
import io.apiorchestrationservice.api.business.dto.inport.DistributedUnLockCommand;
import io.apiorchestrationservice.api.business.dto.outport.DistributedLockInfo;
import io.apiorchestrationservice.api.business.dto.outport.DistributedUnLockInfo;
import io.apiorchestrationservice.api.business.service.DistributedLockService;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/25
 */
@Service
@RequiredArgsConstructor
public class SimpleDistributedLockService implements DistributedLockService {
	private final RedisClientAdapter redisClientAdapter;

	@Override
	public DistributedLockInfo lock(DistributedLockCommand command) {
		return redisClientAdapter.lock(command).confirm();
	}

	@Override
	public DistributedUnLockInfo unlock(DistributedUnLockCommand command) {
		return redisClientAdapter.unlock(command);
	}
}
