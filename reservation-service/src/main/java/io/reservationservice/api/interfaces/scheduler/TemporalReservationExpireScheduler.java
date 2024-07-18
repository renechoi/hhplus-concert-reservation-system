package io.reservationservice.api.interfaces.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import io.reservationservice.api.application.facade.ReservationFacade;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/17
 */
@Component
@RequiredArgsConstructor
public class TemporalReservationExpireScheduler {
	private final ReservationFacade reservationFacade;

	@Scheduled(fixedRateString = "${scheduler.temporalReservationExpireRate}")
	public void cancelExpiredReservations() {
		reservationFacade.cancelExpiredTemporalReservations();
	}
}
