package io.reservationservice.api.infrastructure.clients.redisservice.dto;

import io.reservationservice.api.business.dto.outport.CacheInfo;
import io.reservationservice.common.mapper.ObjectMapperBasedVoMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CacheDomainServiceResponse {
	private String cacheKey;
	private Object cacheValue;
	private Long ttl;
	private Boolean isCached;

	public CacheInfo toCacheInfo() {
		return ObjectMapperBasedVoMapper.convert(this, CacheInfo.class);
	}
}
