package io.reservationservice.api.business.persistence;

import java.util.List;
import java.util.Optional;

import io.reservationservice.api.business.domainentity.Seat;
import io.reservationservice.api.business.dto.inport.SeatSearchCommand;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
public interface SeatRepository {
	Seat save(Seat seat);

	Optional<Seat> findSingleByConditionOptional(SeatSearchCommand searchCommand);
	Seat findSingleByConditionWithThrows(SeatSearchCommand searchCommand);

	List<Seat> findMultipleByCondition(SeatSearchCommand searchCommand);
}
