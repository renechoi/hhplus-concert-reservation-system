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
	public boolean lock(LockCommand lockCommand) {
		RLock lock = redissonClient.getLock(lockCommand.getLockKey());
		try {
			return lock.tryLock(lockCommand.getWaitTime(), lockCommand.getLeaseTime(), lockCommand.getTimeUnit());
		} catch (InterruptedException e) {
			return false;
		}
	}

	/**
	 * <p>
	 * 배경: 일반적으로 Redis의 분산 락은 락을 획득한 스레드만이 해당 락을 해제할 수 있도록 설계되어 있습니다.
	 * 그러나, 특정 요구사항에 따라 락을 보유하고 있지 않은 스레드도 락을 해제해야 할 필요가 있습니다.
	 * 이를 위해 forceUnlock 메서드를 사용하며, 이 메서드는 현재 스레드의 소유 여부에 관계없이 락을 강제로 해제합니다.
	 * </p>
	 * @param unLockCommand
	 * @return
	 */
	@Override
	public boolean unlock(UnLockCommand unLockCommand) {
		RLock lock = redissonClient.getLock(unLockCommand.getLockKey());
		try {
			lock.forceUnlock();
			return true;
		} catch (Exception e) { // 락 해제 실패 시
			return false;
		}
	}
}
