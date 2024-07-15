package io.reservationservice.api.application.facade;

import io.reservationservice.api.application.dto.request.ConcertCreateRequest;
import io.reservationservice.api.application.dto.request.ConcertOptionCreateRequest;
import io.reservationservice.api.application.dto.response.ConcertCreateResponse;
import io.reservationservice.api.application.dto.response.ConcertOptionCreateResponse;
import io.reservationservice.api.business.service.ConcertCrudService;
import io.reservationservice.common.annotation.Facade;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
@Facade
@RequiredArgsConstructor
public class ConcertCrudFacade {
	private final ConcertCrudService concertCrudService;

	public ConcertCreateResponse createConcert(ConcertCreateRequest request) {
		return ConcertCreateResponse.from(concertCrudService.createConcert(request.toCommand()));
	}

	public ConcertOptionCreateResponse createConcertOption(Long concertId, ConcertOptionCreateRequest request) {
		return ConcertOptionCreateResponse.from(concertCrudService.createConcertOption(concertId, request.toCommand()));
	}
}
