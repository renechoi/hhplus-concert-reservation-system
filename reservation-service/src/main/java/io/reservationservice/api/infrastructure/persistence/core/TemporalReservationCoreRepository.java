package io.reservationservice.api.infrastructure.persistence.core;

import java.util.List;

import org.springframework.stereotype.Repository;

import io.reservationservice.api.business.domainentity.TemporalReservation;
import io.reservationservice.api.business.dto.inport.TemporalReservationSearchCommand;
import io.reservationservice.api.business.persistence.TemporalReservationRepository;
import io.reservationservice.api.infrastructure.persistence.orm.TemporalReservationJpaRepository;
import io.reservationservice.common.exception.definitions.TemporalReservationNotFoundException;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
@Repository
@RequiredArgsConstructor
public class TemporalReservationCoreRepository implements TemporalReservationRepository {
	private final TemporalReservationJpaRepository temporalReservationJpaRepository;

	@Override
	public TemporalReservation save(TemporalReservation temporalReservation) {
		return temporalReservationJpaRepository.save(temporalReservation);
	}

	@Override
	public TemporalReservation findById(Long temporalReservationId) {
		return temporalReservationJpaRepository.findById(temporalReservationId).orElseThrow(TemporalReservationNotFoundException::new);
	}

	@Override
	public List<TemporalReservation> findMultipleByCondtion(TemporalReservationSearchCommand searchCommand) {
		return temporalReservationJpaRepository.findMultipleByCondition(searchCommand);
	}

}
