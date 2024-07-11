package io.reservationservice.api.application.facade;

import io.reservationservice.api.application.dto.response.AvailableDatesResponses;
import io.reservationservice.api.application.dto.response.AvailableSeatsResponses;
import io.reservationservice.api.business.service.AvailabilityService;
import io.reservationservice.common.annotation.Facade;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
@Facade
@RequiredArgsConstructor
public class AvailabilityFacade {

	private final AvailabilityService availabilityService;

	public AvailableDatesResponses getAvailableDates(Long concertId) {
		return AvailableDatesResponses.from(availabilityService.getAvailableDates(concertId));
	}

	public AvailableSeatsResponses getAvailableSeats(Long concertOptionId, Long requestAt) {
		return AvailableSeatsResponses.from(availabilityService.getAvailableSeats(concertOptionId, requestAt));
	}
}
