package io.reservationservice.api.business.service.impl;

import static io.reservationservice.api.business.dto.inport.ConcertOptionSearchCommand.*;
import static io.reservationservice.api.business.dto.inport.SeatSearchCommand.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.reservationservice.api.business.domainentity.ConcertOption;
import io.reservationservice.api.business.domainentity.Seat;
import io.reservationservice.api.business.dto.outport.AvailableDateInfo;
import io.reservationservice.api.business.dto.outport.AvailableDateInfos;
import io.reservationservice.api.business.dto.outport.AvailableSeatsInfo;
import io.reservationservice.api.business.dto.outport.AvailableSeatsInfos;
import io.reservationservice.api.business.persistence.ConcertOptionRepository;
import io.reservationservice.api.business.persistence.SeatRepository;
import io.reservationservice.api.business.service.AvailabilityService;
import io.reservationservice.common.annotation.GlobalCacheable;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
@Service
@RequiredArgsConstructor
public class SimpleAvailabilityService implements AvailabilityService {

	private final ConcertOptionRepository concertOptionRepository;
	private final SeatRepository seatRepository;

	@Override
	@Transactional(readOnly = true)
	@GlobalCacheable(prefix = "availableDates", keys = {"#concertId"}, ttl = 60 * 60 * 24)
	public AvailableDateInfos getAvailableDates(Long concertId) {
		List<ConcertOption> concertOptions = concertOptionRepository.findMultipleByCondition(onUpcomingDates(concertId));
		return AvailableDateInfos.from(concertOptions.stream().map(AvailableDateInfo::from).collect(Collectors.toList()));
	}



	@Override
	@Transactional(readOnly = true)
	@GlobalCacheable(prefix = "availableSeats", keys = {"#concertOptionId"}, ttl = 300)
	public AvailableSeatsInfos getAvailableSeats(Long concertOptionId, Long requestAt) {
		List<Seat> seats = seatRepository.findMultipleByCondition(onConcertOption(concertOptionId));

		List<AvailableSeatsInfo> availableSeats = seats.stream()
			.filter(seat -> seat.isAvailableAt(requestAt))
			.map(AvailableSeatsInfo::from)
			.collect(Collectors.toList());

		return AvailableSeatsInfos.from(availableSeats);
	}
}
