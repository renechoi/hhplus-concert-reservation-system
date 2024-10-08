package io.reservationservice.api.infrastructure.persistence.core;

import static io.reservationservice.common.model.GlobalResponseCode.*;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import io.reservationservice.api.business.domainentity.Seat;
import io.reservationservice.api.business.dto.inport.SeatSearchCommand;
import io.reservationservice.api.business.persistence.SeatRepository;
import io.reservationservice.api.infrastructure.persistence.orm.SeatJpaRepository;
import io.reservationservice.common.exception.definitions.ReservationUnAvailableException;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
@Repository
@RequiredArgsConstructor
public class SeatCoreRepository implements SeatRepository {
	private final SeatJpaRepository seatJpaRepository;

	@Override
	public Seat save(Seat seat) {
		return seatJpaRepository.save(seat);
	}

	@Override
	public Optional<Seat> findSingleByConditionOptional(SeatSearchCommand searchCommand) {
		return seatJpaRepository.findSingleByCondition(searchCommand);
	}

	@Override
	public Seat findSingleByCondition(SeatSearchCommand searchCommand) {
		return seatJpaRepository.findSingleByCondition(searchCommand).orElseThrow(() -> new ReservationUnAvailableException(SEAT_NOT_FOUND));
	}

	@Override
	public Seat findSingleByConditionWithLock(SeatSearchCommand searchCommand) {
		return seatJpaRepository.findSingleByConditionWithLock(searchCommand).orElseThrow(() -> new ReservationUnAvailableException(SEAT_NOT_FOUND));
	}

	@Override
	public List<Seat> findMultipleByCondition(SeatSearchCommand searchCommand) {
		return seatJpaRepository.findMultipleByCondition(searchCommand);
	}
}
