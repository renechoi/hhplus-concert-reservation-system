package io.reservationservice.api.infrastructure.persistence.core;

import java.util.List;

import org.springframework.stereotype.Repository;

import io.reservationservice.api.business.domainentity.Reservation;
import io.reservationservice.api.business.dto.inport.ReservationSearchCommand;
import io.reservationservice.api.business.persistence.ReservationRepository;
import io.reservationservice.api.infrastructure.persistence.orm.ReservationJpaRepository;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
@Repository
@RequiredArgsConstructor
public class ReservationCoreRepository implements ReservationRepository {
	private final ReservationJpaRepository reservationJpaRepository;

	@Override
	public Reservation save(Reservation reservation) {
		return reservationJpaRepository.save(reservation);
	}

	@Override
	public List<Reservation> findMultipleBy(ReservationSearchCommand reservationSearchCommand) {
		return reservationJpaRepository.findMultipleByCondition(reservationSearchCommand);
	}

}
