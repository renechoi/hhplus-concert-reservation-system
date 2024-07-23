package io.reservationservice.api.infrastructure.persistence.querydsl;

import java.util.List;

import io.reservationservice.api.business.domainentity.TemporalReservation;
import io.reservationservice.api.business.dto.inport.TemporalReservationSearchCommand;

/**
 * @author : Rene Choi
 * @since : 2024/07/08
 */
public interface TemporalReservationQueryDslCustomRepository {
	List<TemporalReservation> findMultipleByCondition(TemporalReservationSearchCommand searchCommand);

}
