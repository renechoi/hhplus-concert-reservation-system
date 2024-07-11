package io.reservationservice.api.infrastructure.persistence.querydsl;

import java.util.List;
import java.util.Optional;

import io.reservationservice.api.business.domainentity.Seat;
import io.reservationservice.api.business.dto.inport.SeatSearchCommand;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
public interface SeatQueryDslCustomRepository {
	Optional<Seat> findSingleByCondition(SeatSearchCommand searchCommand);

	List<Seat> findMultipleByCondition(SeatSearchCommand searchCommand);

}
