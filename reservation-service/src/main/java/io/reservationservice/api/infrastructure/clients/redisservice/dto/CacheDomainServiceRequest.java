package io.reservationservice.api.infrastructure.clients.redisservice.dto;

import io.reservationservice.api.business.dto.inport.CacheCommand;
import io.reservationservice.common.mapper.ObjectMapperBasedVoMapper;
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