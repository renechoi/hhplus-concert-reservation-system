package io.paymentservice.api.common.cache.business.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author : Rene Choi
 * @since : 2024/07/30
 */
@Data
@AllArgsConstructor
public class LocalCacheEvictEvent {
	private String cacheName;
	private String key;
}