package io.queuemanagement.api.business.dto.inport;

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
public class CacheCommand {
	private String cacheKey;
	private Object cacheValue;
	private long ttl;

	public static CacheCommand of(String cacheKey, Object result, long ttl) {
		return CacheCommand.builder()
				.cacheKey(cacheKey)
				.cacheValue(result)
				.ttl(ttl)
				.build();
	}
}
