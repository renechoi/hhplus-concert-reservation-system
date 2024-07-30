package io.paymentservice.api.common.cache.infrastructure.redisservice.dto;

import io.paymentservice.api.common.cache.business.dto.info.EvictCacheInfo;
import io.paymentservice.common.mapper.ObjectMapperBasedVoMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvictCacheDomainServiceResponse {
	private String cacheKey;
	private Boolean isEvicted;

	public EvictCacheInfo toEvictCacheInfo() {
		 return ObjectMapperBasedVoMapper.convert(this, EvictCacheInfo.class);
	}
}
