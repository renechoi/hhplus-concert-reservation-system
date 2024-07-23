package io.redisservice.api.application.dto;

import java.util.concurrent.TimeUnit;

import org.eclipse.jdt.internal.compiler.codegen.ObjectCache;

import io.redisservice.api.business.service.command.LockCommand;
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
