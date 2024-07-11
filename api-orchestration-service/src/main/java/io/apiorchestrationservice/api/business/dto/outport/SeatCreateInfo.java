package io.apiorchestrationservice.api.business.dto.outport;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author : Rene Choi
 * @since : 2024/07/11
 */
public record SeatCreateInfo(
	Long seatId,
	@JsonIgnore
	ConcertOptionCreateInfo concertOption,
	String section,
	String seatRow,
	String seatNumber,
	Boolean occupied,
	LocalDateTime createdAt
) {
}
