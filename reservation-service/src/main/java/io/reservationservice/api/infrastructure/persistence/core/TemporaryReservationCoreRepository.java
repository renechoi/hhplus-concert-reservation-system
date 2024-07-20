package io.reservationservice.api.infrastructure.persistence.core;

import java.util.List;

import org.springframework.stereotype.Repository;

import io.reservationservice.api.business.domainentity.TemporaryReservation;
import io.reservationservice.api.business.dto.inport.TemporaryReservationSearchCommand;
import io.reservationservice.api.business.persistence.TemporaryReservationRepository;
import io.reservationservice.api.infrastructure.persistence.orm.TemporalReservationJpaRepository;
import io.reservationservice.common.exception.definitions.TemporaryReservationNotFoundException;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
@Repository
@RequiredArgsConstructor
public class TemporaryReservationCoreRepository implements TemporaryReservationRepository {
	private final TemporalReservationJpaRepository temporalReservationJpaRepository;

	@Override
	public TemporaryReservation save(TemporaryReservation temporaryReservation) {
		return temporalReservationJpaRepository.save(temporaryReservation);
	}

	@Override
	public TemporaryReservation findByIdWithThrows(Long temporaryReservationId) {
		return temporalReservationJpaRepository.findById(temporaryReservationId).orElseThrow(TemporaryReservationNotFoundException::new);
	}

	@Override
	public List<TemporaryReservation> findMultipleByCondition(TemporaryReservationSearchCommand searchCommand) {
		return temporalReservationJpaRepository.findMultipleByCondition(searchCommand);
	}

}
