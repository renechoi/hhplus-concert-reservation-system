package io.redisservice.api.business.service;


import static io.redisservice.api.business.dto.info.LockInfo.*;
import static io.redisservice.api.business.dto.info.UnLockInfo.*;

import org.springframework.stereotype.Service;

import io.redisservice.api.business.repository.RedisLockRepository;
import io.redisservice.api.business.dto.command.LockCommand;
import io.redisservice.api.business.dto.command.UnLockCommand;
import io.redisservice.api.business.dto.info.LockInfo;
import io.redisservice.api.business.dto.info.UnLockInfo;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/23
 */
@Service
@RequiredArgsConstructor
public class RedisLockService {

	private final RedisLockRepository redisLockRepository;

	public LockInfo lock(LockCommand lockCommand) {
		boolean locked = redisLockRepository.lock(lockCommand);
		return createLockInfo(lockCommand, locked);
	}

	public UnLockInfo unlock(UnLockCommand unLockCommand) {
		boolean unlocked = redisLockRepository.unlock(unLockCommand);
		return createUnLockInfo(unLockCommand, unlocked);
	}
}
