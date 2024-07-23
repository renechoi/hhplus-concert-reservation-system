package io.redisservice.api.business.service.info;

import java.util.concurrent.TimeUnit;

import io.redisservice.api.business.service.command.UnLockCommand;

/**
 * @author : Rene Choi
 * @since : 2024/07/23
 */
public record UnLockInfo(
	String lockKey,
	boolean isUnLocked
) {
	public static UnLockInfo createUnLockInfo(UnLockCommand unLockCommand, boolean isUnlocked) {
		return new UnLockInfo(unLockCommand.getLockKey(), isUnlocked);
	}
}
