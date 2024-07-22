package io.reservationservice.api.business.persistence;

import java.util.List;

import io.reservationservice.api.business.domainentity.TemporaryReservation;
import io.reservationservice.api.business.dto.inport.TemporaryReservationSearchCommand;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
public interface TemporaryReservationRepository {
	TemporaryReservation save(TemporaryReservation temporaryReservation);
	TemporaryReservation findById(Long temporaryReservationId);
	List<TemporaryReservation> findMultipleBy(TemporaryReservationSearchCommand temporaryReservationSearchCommand);
}
