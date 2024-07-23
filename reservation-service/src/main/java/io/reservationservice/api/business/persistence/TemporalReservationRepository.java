package io.reservationservice.api.business.persistence;

import java.util.List;

import io.reservationservice.api.business.domainentity.TemporalReservation;
import io.reservationservice.api.business.dto.inport.TemporalReservationSearchCommand;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
public interface TemporalReservationRepository {
	TemporalReservation save(TemporalReservation temporalReservation);
	TemporalReservation findById(Long temporalReservationId);
	List<TemporalReservation> findMultipleByCondtion(TemporalReservationSearchCommand temporalReservationSearchCommand);
}
