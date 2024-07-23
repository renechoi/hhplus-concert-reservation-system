package io.redisservice.api.application.dto;

import io.redisservice.api.business.service.command.UnLockCommand;
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
public class UnLockRequest {
	private String lockKey;

	public UnLockCommand toCommand() {
		return ObjectMapperBasedVoMapper.convert(this, UnLockCommand.class);
	}
}