package io.redisservice.api.application.dto.request;

import java.util.concurrent.TimeUnit;

import io.redisservice.api.business.dto.command.LockCommand;
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
public class LockRequest {
	private String lockKey;
	private long waitTime;
	private long leaseTime;
	private TimeUnit timeUnit;

	public LockCommand toCommand() {
		return ObjectMapperBasedVoMapper.convert(this, LockCommand.class);
	}
}
