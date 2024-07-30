package io.paymentservice.api.common.cache.infrastructure.redisservice.dto;

import io.paymentservice.api.common.cache.business.dto.info.CacheInfo;
import io.paymentservice.common.mapper.ObjectMapperBasedVoMapper;
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
