package io.queuemanagement.api.infrastructure.clients.redisservice.dto;

import io.queuemanagement.api.business.dto.outport.EvictCacheInfo;
import io.queuemanagement.common.mapper.ObjectMapperBasedVoMapper;
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
