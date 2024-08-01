package io.queuemanagement.api.infrastructure.clients.redisservice.dto;

import io.queuemanagement.api.business.dto.inport.EvictCacheCommand;
import io.queuemanagement.common.mapper.ObjectMapperBasedVoMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvictCacheDomainServiceRequest {
	private String cacheKey;

	public static EvictCacheDomainServiceRequest from(EvictCacheCommand command) {
		return ObjectMapperBasedVoMapper.convert(command, EvictCacheDomainServiceRequest.class);
	}
}
