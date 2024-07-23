package io.redisservice.api.application.dto;

import io.redisservice.api.business.service.info.LockInfo;
import io.redisservice.common.mapper.ObjectMapperBasedVoMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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