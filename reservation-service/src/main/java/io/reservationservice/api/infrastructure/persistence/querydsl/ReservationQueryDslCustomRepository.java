package io.reservationservice.api.infrastructure.persistence.querydsl;

import java.util.List;

import io.reservationservice.api.business.domainentity.Reservation;
import io.reservationservice.api.business.dto.inport.ReservationSearchCommand;

/**
 * @author : Rene Choi
 * @since : 2024/07/08
 */
public interface ReservationQueryDslCustomRepository {
	List<Reservation> findMultipleByCondition(ReservationSearchCommand searchCommand);

}
