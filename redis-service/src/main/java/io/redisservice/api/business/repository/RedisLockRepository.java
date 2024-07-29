package io.redisservice.api.business.repository;

import io.redisservice.api.business.dto.command.LockCommand;
import io.redisservice.api.business.dto.command.UnLockCommand;

/**
 * @author : Rene Choi
 * @since : 2024/07/23
 */
public interface RedisLockRepository {
	boolean lock(LockCommand lockRequestDTO);

	boolean unlock(UnLockCommand unLockCommand);
}
