package io.reservationservice.api.business.persistence;

import java.util.List;

import io.reservationservice.api.business.domainentity.Reservation;
import io.reservationservice.api.business.dto.inport.ReservationSearchCommand;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
public interface ReservationRepository {
	Reservation save(Reservation reservation);

	List<Reservation> findMultipleBy(ReservationSearchCommand reservationSearchCommand);

}
