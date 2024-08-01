package io.reservationservice.api.application.eventlistener;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import io.reservationservice.api.business.dto.event.LocalCacheEvictEvent;
import io.reservationservice.api.business.dto.inport.PropagateCacheEvictionCommand;
import io.reservationservice.api.business.service.impl.CacheEvictionPropagationService;
import io.reservationservice.api.business.service.impl.LocalCacheEvictionService;
import io.reservationservice.api.infrastructure.clients.discovery.DiscoveryInstanceClientAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : Rene Choi
 * @since : 2024/07/30
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CacheEvictEventListener {

	private final CacheEvictionPropagationService cacheEvictionPropagationService;

	@EventListener
	@Async
	public void handleLocalCacheEvictEvent(LocalCacheEvictEvent event) {
		try {
			cacheEvictionPropagationService.propagateCacheEviction(PropagateCacheEvictionCommand.of(event.getCacheName(), event.getKey()));
		} catch (Exception e) {
			log.error("Failed to evict cache for LocalCacheEvictEvent: {} - {} - {}", event.getCacheName(),event.getKey(), e.getMessage());
		}
	}
}
