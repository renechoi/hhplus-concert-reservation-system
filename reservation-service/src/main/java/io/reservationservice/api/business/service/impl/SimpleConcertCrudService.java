package io.reservationservice.api.business.service.impl;

import static io.reservationservice.api.business.domainentity.Seat.*;

import java.util.stream.IntStream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.reservationservice.api.business.domainentity.Concert;
import io.reservationservice.api.business.domainentity.ConcertOption;
import io.reservationservice.api.business.dto.inport.ConcertCreateCommand;
import io.reservationservice.api.business.dto.inport.ConcertOptionCreateCommand;
import io.reservationservice.api.business.dto.outport.ConcertCreateInfo;
import io.reservationservice.api.business.dto.outport.ConcertOptionCreateInfo;
import io.reservationservice.api.business.persistence.ConcertOptionRepository;
import io.reservationservice.api.business.persistence.ConcertRepository;
import io.reservationservice.api.business.persistence.SeatRepository;
import io.reservationservice.api.business.service.ConcertCrudService;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
@Service
@RequiredArgsConstructor
public class SimpleConcertCrudService implements ConcertCrudService {
	private final ConcertRepository concertRepository;
	private final ConcertOptionRepository concertOptionRepository;
	private final SeatRepository seatRepository;

	@Override
	@Transactional
	public ConcertCreateInfo createConcert(ConcertCreateCommand command) {
		return ConcertCreateInfo.from(concertRepository.save(command.toEntity()));
	}

	@Transactional
	public ConcertOptionCreateInfo createConcertOption(Long concertId, ConcertOptionCreateCommand command) {
		Concert concert = concertRepository.findById(concertId);
		ConcertOption concertOption = command.toEntityWithConcert(concert);
		concertOptionRepository.save(concertOption);

		IntStream.rangeClosed(1, command.getMaxSeats())
			.mapToObj(i -> createSeatWithConcertOptionAndNumber(concertOption, (long) i))
			.forEach(seatRepository::save);

		return ConcertOptionCreateInfo.from(concertOption);
	}
}
