package io.redisservice.api.application.dto.response;

import io.redisservice.api.business.dto.info.CacheInfo;
import io.redisservice.common.mapper.ObjectMapperBasedVoMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/30
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CacheResponse {
	private String cacheKey;
	private Object cacheValue;
	private Long ttl;
	private Boolean isCached;

	public static CacheResponse from(CacheInfo cacheInfo) {
		return ObjectMapperBasedVoMapper.convert(cacheInfo, CacheResponse.class);
	}
}
