package io.apiorchestrationservice.api.infrastructure.clients.reservation.dto;

import java.time.LocalDateTime;

import io.apiorchestrationservice.api.business.dto.outport.ConcertOptionInfo;

/**
 * @author : Rene Choi
 * @since : 2024/07/08
 */
public record AvailableSeatsDomainServiceInfo(
	 Long seatId,
	 ConcertOptionInfo concertOption,
	 Long seatNumber,
	 Boolean occupied,
	 LocalDateTime createdAt

) {

}
