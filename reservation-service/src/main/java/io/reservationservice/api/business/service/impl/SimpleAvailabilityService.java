package io.reservationservice.api.business.service.impl;

import static io.reservationservice.api.business.dto.inport.ConcertOptionSearchCommand.*;
import static io.reservationservice.api.business.dto.inport.SeatSearchCommand.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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

	// todo -> 요구 사항에 따라 seat 정보도 (seat total count / available seats를 리턴할지 판단)
	@Override
	@Transactional(readOnly = true)
	public AvailableDateInfos getAvailableDates(Long concertId) {
		List<ConcertOption> concertOptions = concertOptionRepository.findMultipleByCondition(searchByConcertIdWithFutureConcertDates(concertId));
		return AvailableDateInfos.from(concertOptions.stream().map(AvailableDateInfo::from).collect(Collectors.toList()));
	}



	@Override
	@Transactional(readOnly = true)
	public AvailableSeatsInfos getAvailableSeats(Long concertOptionId, Long requestAt) {
		LocalDateTime date = Instant.ofEpochMilli(requestAt).atZone(ZoneOffset.UTC).toLocalDateTime();
		List<Seat> seats = seatRepository.findMultipleByCondition(searchByConcertOptionId(concertOptionId));

		List<AvailableSeatsInfo> availableSeats = seats.stream()
			.filter(seat -> seat.isAvailable(date))
			.map(AvailableSeatsInfo::from)
			.collect(Collectors.toList());

		return AvailableSeatsInfos.from(availableSeats);
	}
}
