package io.reservationservice.api.business.service.impl;

import org.springframework.stereotype.Service;

import io.reservationservice.api.business.dto.inport.PropagateCacheEvictionCommand;
import io.reservationservice.api.infrastructure.clients.discovery.DiscoveryInstanceClientAdapter;
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
