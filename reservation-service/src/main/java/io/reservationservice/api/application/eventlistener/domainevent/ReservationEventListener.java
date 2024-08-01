package io.reservationservice.api.application.eventlistener.domainevent;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import io.reservationservice.api.business.dto.event.ReservationChangedEvent;
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
public class ReservationEventListener {

	private final CacheEvictionPropagationService propagationService;

	@EventListener
	@Async
	public void handleReservationChangedEvent(ReservationChangedEvent event) {
		evictReservationCache(event.getUserId(), event.getConcertOptionId());
	}

	private void evictReservationCache(Long userId, Long concertOptionId) {
		try {
			propagationService.propagateCacheEviction(PropagateCacheEvictionCommand.of("reservationStatus", userId + "-" + concertOptionId));
			log.info("Evicted cache for userId: {} and concertOptionId: {}", userId, concertOptionId);
		} catch (Exception e) {
			log.error("Failed to evict cache for userId: {} and concertOptionId: {}. Exception: {}", userId, concertOptionId, e.getMessage());
		}
	}


}