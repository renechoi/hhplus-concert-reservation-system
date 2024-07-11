package io.reservationservice.api.business.service;

import org.springframework.transaction.annotation.Transactional;

import io.reservationservice.api.business.dto.outport.AvailableDateInfos;
import io.reservationservice.api.business.dto.outport.AvailableSeatsInfos;

/**
 * @author : Rene Choi
 * @since : 2024/07/08
 */
public interface AvailabilityService {
	@Transactional(readOnly = true)
	AvailableDateInfos getAvailableDates(Long concertId);

	@Transactional(readOnly = true)
	AvailableSeatsInfos getAvailableSeats(Long concertOptionId, Long requestAt);
}
