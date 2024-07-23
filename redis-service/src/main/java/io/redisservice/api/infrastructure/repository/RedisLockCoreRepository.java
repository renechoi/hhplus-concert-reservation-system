package io.redisservice.api.infrastructure.repository;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import io.redisservice.api.business.RedisLockRepository;
import io.redisservice.api.business.service.command.LockCommand;
import io.redisservice.api.business.service.command.UnLockCommand;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/23
 */
@Component
@RequiredArgsConstructor
public class RedisLockCoreRepository implements RedisLockRepository {

	private final RedissonClient redissonClient;

	@Override
	public boolean lock(LockCommand lockRequestDTO) {
		RLock lock = redissonClient.getLock(lockRequestDTO.getLockKey());
		try {
			return lock.tryLock(lockRequestDTO.getWaitTime(), lockRequestDTO.getLeaseTime(), lockRequestDTO.getTimeUnit());
		} catch (InterruptedException e) {
			return false;
		}
	}


	@Override
	public boolean unlock(UnLockCommand unLockCommand) {
		RLock lock = redissonClient.getLock(unLockCommand.getLockKey());
		if (lock.isHeldByCurrentThread()) {
			lock.unlock();
			return true;
		}
		return false;
	}
}
