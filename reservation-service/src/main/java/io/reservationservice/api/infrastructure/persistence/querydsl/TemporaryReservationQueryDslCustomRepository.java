package io.reservationservice.api.infrastructure.persistence.querydsl;

import java.util.List;

import io.reservationservice.api.business.domainentity.TemporaryReservation;
import io.reservationservice.api.business.dto.inport.TemporaryReservationSearchCommand;

/**
 * @author : Rene Choi
 * @since : 2024/07/08
 */
public interface TemporaryReservationQueryDslCustomRepository {
	List<TemporaryReservation> findMultipleByCondition(TemporaryReservationSearchCommand searchCommand);

}
