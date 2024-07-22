package io.reservationservice.api.business.persistence;

import java.util.List;

import io.reservationservice.api.business.domainentity.ConcertOption;
import io.reservationservice.api.business.dto.inport.ConcertOptionSearchCommand;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
public interface ConcertOptionRepository {
	ConcertOption save(ConcertOption entityWithConcert);

	ConcertOption findById(Long concertOptionId);

	List<ConcertOption> findMultipleBy(ConcertOptionSearchCommand searchCommand);
}
