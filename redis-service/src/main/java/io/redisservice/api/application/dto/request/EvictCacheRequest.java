package io.redisservice.api.application.dto.request;

import io.redisservice.api.business.dto.command.EvictCacheCommand;
import io.redisservice.common.mapper.ObjectMapperBasedVoMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvictCacheRequest {
	private String cacheKey;

	public static EvictCacheRequest from(EvictCacheCommand command) {
		return ObjectMapperBasedVoMapper.convert(command, EvictCacheRequest.class);
	}

	public EvictCacheCommand toEvictCacheCommand() {
		return ObjectMapperBasedVoMapper.convert(this, EvictCacheCommand.class);
	}
}
