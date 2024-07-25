package io.apiorchestrationservice.api.infrastructure.clients.reservation;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.apiorchestrationservice.api.infrastructure.clients.reservation.dto.AvailableDatesDomainServiceResponses;
import io.apiorchestrationservice.api.infrastructure.clients.reservation.dto.AvailableSeatsDomainServiceResponses;
import io.apiorchestrationservice.api.infrastructure.clients.reservation.dto.ConcertCreateDomainServiceRequest;
import io.apiorchestrationservice.api.infrastructure.clients.reservation.dto.ConcertCreateDomainServiceResponse;
import io.apiorchestrationservice.api.infrastructure.clients.reservation.dto.ConcertOptionCreateDomainServiceRequest;
import io.apiorchestrationservice.api.infrastructure.clients.reservation.dto.ConcertOptionCreateDomainServiceResponse;
import io.apiorchestrationservice.api.infrastructure.clients.reservation.dto.ReservationConfirmDomainServiceRequest;
import io.apiorchestrationservice.api.infrastructure.clients.reservation.dto.ReservationConfirmDomainServiceResponse;
import io.apiorchestrationservice.api.infrastructure.clients.reservation.dto.ReservationCreateDomainServiceRequest;
import io.apiorchestrationservice.api.infrastructure.clients.reservation.dto.ReservationStatusDomainServiceResponses;
import io.apiorchestrationservice.api.infrastructure.clients.reservation.dto.TemporalReservationCreateDomainServiceResponse;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */
@FeignClient(
	name = "reservation-service",
	path = "/reservation"

)
public interface ReservationServiceClient {


	// 콘서트
	@PostMapping("/api/concerts")
	ResponseEntity<ConcertCreateDomainServiceResponse> createConcert(@RequestBody ConcertCreateDomainServiceRequest request);

	@PostMapping("/api/concerts/{concertId}/options")
	ResponseEntity<ConcertOptionCreateDomainServiceResponse> createConcertOption(@PathVariable Long concertId, @RequestBody ConcertOptionCreateDomainServiceRequest request);



	// 예약
	@PostMapping(value =  "/api/reservations")
	ResponseEntity<TemporalReservationCreateDomainServiceResponse> createTemporalReservation(@RequestBody ReservationCreateDomainServiceRequest request);




	// 예약 확정
	@PostMapping(value = "/api/reservations/confirm")
	ResponseEntity<ReservationConfirmDomainServiceResponse> confirmReservation(@RequestBody ReservationConfirmDomainServiceRequest request);

	// 예약 상태 조회
	@GetMapping(value = "/api/reservations/status/{userId}/{concertOptionId}")
	ResponseEntity<ReservationStatusDomainServiceResponses> getReservationStatus(@PathVariable Long userId, @PathVariable Long concertOptionId);





	// 가능한 날짜 좌석 조회
	@GetMapping("/api/availability/dates/{concertId}")
	ResponseEntity<AvailableDatesDomainServiceResponses> getAvailableDates(@PathVariable Long concertId);

	@GetMapping("/api/availability/seats/{concertOptionId}/{requestAt}")
	ResponseEntity<AvailableSeatsDomainServiceResponses> getAvailableSeats(@PathVariable Long concertOptionId, @PathVariable Long requestAt);
}
