package io.redisservice.api.business.service.info;

import java.util.concurrent.TimeUnit;

import io.redisservice.api.business.service.command.LockCommand;

/**
 * @author : Rene Choi
 * @since : 2024/07/23
 */
public record LockInfo(
	String lockKey,
	long waitTime,
	long leaseTime,
	TimeUnit timeUnit,
	boolean isLocked
) {

	public static LockInfo createLockInfo(LockCommand lockCommand, boolean isLocked) {
		return new LockInfo(lockCommand.getLockKey(), lockCommand.getWaitTime(), lockCommand.getLeaseTime(), lockCommand.getTimeUnit(), isLocked);
	}
}
