package io.paymentservice.api.common.cache.business.service;

import org.springframework.stereotype.Service;

import io.paymentservice.api.common.cache.business.dto.command.PropagateCacheEvictionCommand;
import io.paymentservice.api.common.cache.infrastructure.discovery.DiscoveryInstanceClientAdapter;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/31
 */
@Service
@RequiredArgsConstructor
public class CacheEvictionPropagationService {

	private final DiscoveryInstanceClientAdapter discoveryInstanceClientAdapter;

	public void propagateCacheEviction(PropagateCacheEvictionCommand command) {

		String commandPath = String.format("/api/v1/cache/evict/%s/%s", command.getCacheName(), command.getKey());
		discoveryInstanceClientAdapter.getRequestToAllInstances(commandPath);
	}
}
