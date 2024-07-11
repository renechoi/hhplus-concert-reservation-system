package io.apiorchestrationservice.api.infrastructure.clients.reservation.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * @author : Rene Choi
 * @since : 2024/07/08
 */
public record SeatCreateDomainServiceResponse(
	Long seatId,
	@JsonIgnore
	ConcertOptionCreateDomainServiceResponse concertOption,
	String section,
	String seatRow,
	String seatNumber,
	Boolean occupied,
	LocalDateTime createdAt
) {
}
