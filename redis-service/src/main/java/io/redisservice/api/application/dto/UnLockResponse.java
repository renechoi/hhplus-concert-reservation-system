package io.redisservice.api.application.dto;

import io.redisservice.api.business.service.info.UnLockInfo;
import io.redisservice.common.mapper.ObjectMapperBasedVoMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/23
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnLockResponse {
	private String lockKey;
	private boolean isUnlocked;

	public static UnLockResponse from(UnLockInfo unLockInfo) {
		return ObjectMapperBasedVoMapper.convert(unLockInfo, UnLockResponse.class);
	}
}