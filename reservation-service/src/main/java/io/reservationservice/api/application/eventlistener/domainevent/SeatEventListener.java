package io.reservationservice.api.application.eventlistener.domainevent;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import io.reservationservice.api.business.dto.event.SeatChangedEvent;
import io.reservationservice.api.business.dto.inport.EvictCacheCommand;
import io.reservationservice.api.business.dto.inport.PropagateCacheEvictionCommand;
import io.reservationservice.api.business.service.impl.CacheEvictionPropagationService;
import io.reservationservice.api.business.service.impl.RedisCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : Rene Choi
 * @since : 2024/07/30
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class SeatEventListener {

	private final RedisCacheService cacheService;

	@EventListener
	@Async
	public void handleSeatReservedEvent(SeatChangedEvent event) {
		evictAvailableSeatsCache(event.getConcertOptionId());
	}


	private void evictAvailableSeatsCache(Long concertOptionId) {
		try {
			cacheService.evict(EvictCacheCommand.builder().cacheKey("availableSeats-" + concertOptionId).build());
			log.info("Evicted cache for concertOptionId: {}", concertOptionId);
		} catch (Exception e) {
			log.error("Failed to evict cache for concertOptionId: {}. Exception: {}", concertOptionId, e.getMessage());
		}
	}
}