package io.reservationservice.api.business.dto.inport;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/31
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropagateCacheEvictionCommand {
	private String cacheName;
	private String key;
	public static PropagateCacheEvictionCommand of(String cacheName, String key) {
		return PropagateCacheEvictionCommand.builder()
				.cacheName(cacheName)
				.key(key)
				.build();
	}
}
