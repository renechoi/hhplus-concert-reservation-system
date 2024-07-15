package io.apiorchestrationservice.api.application.facade;

import io.apiorchestrationservice.api.application.dto.request.AvailableSeatsRetrievalRequest;
import io.apiorchestrationservice.api.application.dto.request.ConcertCreateRequest;
import io.apiorchestrationservice.api.application.dto.request.ConcertOptionCreateRequest;
import io.apiorchestrationservice.api.application.dto.response.AvailableDatesResponses;
import io.apiorchestrationservice.api.application.dto.response.AvailableSeatsResponses;
import io.apiorchestrationservice.api.application.dto.response.ConcertCreateResponse;
import io.apiorchestrationservice.api.application.dto.response.ConcertOptionCreateResponse;
import io.apiorchestrationservice.api.business.service.ConcertService;
import io.apiorchestrationservice.common.annotation.Facade;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/11
 */
@Facade
@RequiredArgsConstructor
public class ConcertFacade {
	private final ConcertService concertService;

	public ConcertCreateResponse createConcert(ConcertCreateRequest request) {
		return ConcertCreateResponse.from(concertService.createConcert(request.toCommand()));
	}

	public ConcertOptionCreateResponse createConcertOption(Long concertId, ConcertOptionCreateRequest request) {
		return ConcertOptionCreateResponse.from(concertService.createConcertOption(concertId, request.toCommand()));
	}

	public AvailableDatesResponses getAvailableDates(Long concertOptionId) {
		return AvailableDatesResponses.from(concertService.getAvailableDates(concertOptionId));
	}

	public AvailableSeatsResponses getAvailableSeats(AvailableSeatsRetrievalRequest request) {
		return AvailableSeatsResponses.from(concertService.getAvailableSeats(request.toCommand()));
	}
}
