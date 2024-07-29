package io.redisservice.api.application.dto.response;

import io.redisservice.api.business.dto.info.LockInfo;
import io.redisservice.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/07/23
 */

public record LockResponse(
	String lockKey,
	Long waitTime,
	Long leaseTime,
	Boolean isLocked
) {

	public static LockResponse from(LockInfo lockInfo) {
		return ObjectMapperBasedVoMapper.convert(lockInfo, LockResponse.class);
	}
}