package io.reservationservice.api.infrastructure.clients.redisservice.dto;

import io.reservationservice.api.business.dto.inport.EvictCacheCommand;
import io.reservationservice.common.mapper.ObjectMapperBasedVoMapper;
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
