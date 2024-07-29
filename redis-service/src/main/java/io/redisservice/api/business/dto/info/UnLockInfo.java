package io.redisservice.api.business.dto.info;

import io.redisservice.api.business.dto.command.UnLockCommand;

/**
 * @author : Rene Choi
 * @since : 2024/07/23
 */
public record UnLockInfo(
	String lockKey,
	Boolean isUnLocked
) {
	public static UnLockInfo createUnLockInfo(UnLockCommand unLockCommand, boolean isUnlocked) {
		return new UnLockInfo(unLockCommand.getLockKey(), isUnlocked);
	}
}
