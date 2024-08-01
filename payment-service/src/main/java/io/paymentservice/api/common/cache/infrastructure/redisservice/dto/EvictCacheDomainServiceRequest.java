package io.paymentservice.api.common.cache.infrastructure.redisservice.dto;

import io.paymentservice.api.common.cache.business.dto.command.EvictCacheCommand;
import io.paymentservice.common.mapper.ObjectMapperBasedVoMapper;
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
