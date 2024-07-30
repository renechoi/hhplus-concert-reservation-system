package io.reservationservice.api.application.eventlistener.domainevent;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import io.reservationservice.api.business.dto.event.TemporalReservationChangedEvent;
import io.reservationservice.api.business.dto.inport.EvictCacheCommand;
import io.reservationservice.api.business.dto.inport.PropagateCacheEvictionCommand;
import io.reservationservice.api.business.service.impl.CacheEvictionPropagationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : Rene Choi
 * @since : 2024/07/31
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TemporalReservationEventListener {

	private final CacheEvictionPropagationService cacheEvictionPropagationService;

	@EventListener
	@Async
	public void handleTemporalReservationChangedEvent(TemporalReservationChangedEvent event) {
		evictTemporalReservationCache(event.getUserId(), event.getConcertOptionId());
	}

	private void evictTemporalReservationCache(Long userId, Long concertOptionId) {
		try {
			cacheEvictionPropagationService.propagateCacheEviction(PropagateCacheEvictionCommand.of("reservationStatus", userId + "-" + concertOptionId));
			log.info("Evicted cache for userId: {} and concertOptionId: {}", userId, concertOptionId);
		} catch (Exception e) {
			log.error("Failed to evict cache for userId: {} and concertOptionId: {}. Exception: {}", userId, concertOptionId, e.getMessage());
		}
	}



}