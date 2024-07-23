package io.redisservice.api.business;

import io.redisservice.api.business.service.command.LockCommand;
import io.redisservice.api.business.service.command.UnLockCommand;

/**
 * @author : Rene Choi
 * @since : 2024/07/23
 */
public interface RedisLockRepository {
	boolean lock(LockCommand lockRequestDTO);

	boolean unlock(UnLockCommand unLockCommand);
}
