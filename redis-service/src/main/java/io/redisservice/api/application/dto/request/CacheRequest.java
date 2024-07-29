package io.redisservice.api.application.dto.request;

import io.redisservice.api.business.dto.command.CacheCommand;
import io.redisservice.common.mapper.ObjectMapperBasedVoMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/30
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CacheRequest {
	private String cacheKey;
	private Object cacheValue;
	private long ttl;

	public  CacheCommand toCommand() {
		return ObjectMapperBasedVoMapper.convert(this, CacheCommand.class);
	}
}
