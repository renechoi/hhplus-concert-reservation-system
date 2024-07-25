package io.apiorchestrationservice.api.infrastructure.clients.reservation;

import java.util.Optional;

import org.springframework.http.ResponseEntity;

import io.apiorchestrationservice.api.business.client.ReservationServiceClientAdapter;
import io.apiorchestrationservice.api.business.dto.inport.ConcertCreateCommand;
import io.apiorchestrationservice.api.business.dto.inport.ConcertOptionCreateCommand;
import io.apiorchestrationservice.api.business.dto.inport.ReservationConfirmCommand;
import io.apiorchestrationservice.api.business.dto.inport.TemporalReservationCreateCommand;
import io.apiorchestrationservice.api.business.dto.outport.AvailableDatesInfos;
import io.apiorchestrationservice.api.business.dto.outport.ConcertCreateInfo;
import io.apiorchestrationservice.api.business.dto.outport.ConcertOptionCreateInfo;
import io.apiorchestrationservice.api.business.dto.outport.ReservationConfirmInfo;
import io.apiorchestrationservice.api.business.dto.outport.ReservationStatusInfos;
import io.apiorchestrationservice.api.business.dto.outport.TemporalReservationCreateInfo;
import io.apiorchestrationservice.api.infrastructure.clients.reservation.dto.AvailableDatesDomainServiceResponses;
import io.apiorchestrationservice.api.infrastructure.clients.reservation.dto.AvailableSeatsDomainServiceResponses;
import io.apiorchestrationservice.api.infrastructure.clients.reservation.dto.AvailableSeatsInfos;
import io.apiorchestrationservice.api.infrastructure.clients.reservation.dto.ConcertCreateDomainServiceRequest;
import io.apiorchestrationservice.api.infrastructure.clients.reservation.dto.ConcertCreateDomainServiceResponse;
import io.apiorchestrationservice.api.infrastructure.clients.reservation.dto.ConcertOptionCreateDomainServiceRequest;
import io.apiorchestrationservice.api.infrastructure.clients.reservation.dto.ConcertOptionCreateDomainServiceResponse;
import io.apiorchestrationservice.api.infrastructure.clients.reservation.dto.ReservationConfirmDomainServiceRequest;
import io.apiorchestrationservice.api.infrastructure.clients.reservation.dto.ReservationConfirmDomainServiceResponse;
import io.apiorchestrationservice.api.infrastructure.clients.reservation.dto.ReservationCreateDomainServiceRequest;
import io.apiorchestrationservice.api.infrastructure.clients.reservation.dto.ReservationStatusDomainServiceResponses;
import io.apiorchestrationservice.api.infrastructure.clients.reservation.dto.TemporalReservationCreateDomainServiceResponse;
import io.apiorchestrationservice.common.annotation.FeignAdapter;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */

@FeignAdapter
@RequiredArgsConstructor
public class ReservationServiceFeignClientAdapter implements ReservationServiceClientAdapter {

	private final ReservationServiceClient reservationServiceClient;


	@Override
	public ConcertCreateInfo createConcert(ConcertCreateCommand command) {
		ResponseEntity<ConcertCreateDomainServiceResponse> response = reservationServiceClient.createConcert(ConcertCreateDomainServiceRequest.from(command));
		return Optional.ofNullable(response.getBody())
			.map(ConcertCreateDomainServiceResponse::toConcertCreateInfo)
			.orElseThrow();
	}

	@Override
	public ConcertOptionCreateInfo createConcertOption(ConcertOptionCreateCommand command) {
		ConcertOptionCreateDomainServiceRequest request = ConcertOptionCreateDomainServiceRequest.from(command);
		ResponseEntity<ConcertOptionCreateDomainServiceResponse> response = reservationServiceClient.createConcertOption(command.getConcertId(), request);
		return Optional.ofNullable(response.getBody())
			.map(ConcertOptionCreateDomainServiceResponse::toConcertOptionCreateInfo)
			.orElseThrow();
	}

	@Override
	public TemporalReservationCreateInfo createTemporalReservation(TemporalReservationCreateCommand command) {
		ReservationCreateDomainServiceRequest request = ReservationCreateDomainServiceRequest.from(command);
		ResponseEntity<TemporalReservationCreateDomainServiceResponse> response = reservationServiceClient.createTemporalReservation(request);
		return Optional.ofNullable(response.getBody())
			.map(TemporalReservationCreateDomainServiceResponse::toTemporalReservationCreateInfo)
			.orElseThrow();
	}



	@Override
	public ReservationConfirmInfo confirmReservation(ReservationConfirmCommand command) {
		ReservationConfirmDomainServiceRequest request = ReservationConfirmDomainServiceRequest.from(command);
		ResponseEntity<ReservationConfirmDomainServiceResponse> response = reservationServiceClient.confirmReservation(request);
		return Optional.ofNullable(response.getBody())
			.map(ReservationConfirmDomainServiceResponse::toReservationConfirmInfo)
			.orElseThrow();
	}

	@Override
	public ReservationStatusInfos getReservationStatus(Long userId, Long concertOptionId) {
		ResponseEntity<ReservationStatusDomainServiceResponses> response = reservationServiceClient.getReservationStatus(userId, concertOptionId);
		return Optional.ofNullable(response.getBody())
			.map(ReservationStatusDomainServiceResponses::toReservationStatusInfos)
			.orElseThrow();
	}




	@Override
	public AvailableDatesInfos getAvailableDates(Long concertId) {
		ResponseEntity<AvailableDatesDomainServiceResponses> response = reservationServiceClient.getAvailableDates(concertId);
		return Optional.ofNullable(response.getBody())
			.map(AvailableDatesDomainServiceResponses::toAvailableDatesInfo)
			.orElseThrow();
	}

	@Override
	public AvailableSeatsInfos getAvailableSeats(Long concertOptionId, Long requestAt) {
		ResponseEntity<AvailableSeatsDomainServiceResponses> response = reservationServiceClient.getAvailableSeats(concertOptionId, requestAt);
		return Optional.ofNullable(response.getBody())
			.map(AvailableSeatsDomainServiceResponses::toAvailableSeatsInfo)
			.orElseThrow();

	}
}