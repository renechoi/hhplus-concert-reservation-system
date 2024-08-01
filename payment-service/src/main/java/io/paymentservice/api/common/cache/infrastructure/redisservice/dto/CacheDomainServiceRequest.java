package io.paymentservice.api.common.cache.infrastructure.redisservice.dto;

import io.paymentservice.api.common.cache.business.dto.command.CacheCommand;
import io.paymentservice.common.mapper.ObjectMapperBasedVoMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CacheDomainServiceRequest {
	private String cacheKey;
	private Object cacheValue;
	private long ttl;

	public static CacheDomainServiceRequest from(CacheCommand command) {
		return ObjectMapperBasedVoMapper.convert(command, CacheDomainServiceRequest.class);
	}
}