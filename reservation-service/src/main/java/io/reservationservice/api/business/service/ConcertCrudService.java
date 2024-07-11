package io.reservationservice.api.business.service;

import io.reservationservice.api.business.dto.inport.ConcertCreateCommand;
import io.reservationservice.api.business.dto.inport.ConcertOptionCreateCommand;
import io.reservationservice.api.business.dto.outport.ConcertCreateInfo;
import io.reservationservice.api.business.dto.outport.ConcertOptionCreateInfo;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
public interface ConcertCrudService {
	ConcertCreateInfo createConcert(ConcertCreateCommand command);

	ConcertOptionCreateInfo createConcertOption(Long concertId, ConcertOptionCreateCommand command);
}
