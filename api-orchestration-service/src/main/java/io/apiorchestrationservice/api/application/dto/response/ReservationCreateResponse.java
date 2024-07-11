package io.apiorchestrationservice.api.application.dto.response;

import java.time.LocalDateTime;

import io.apiorchestrationservice.api.business.dto.outport.ConcertOptionCreateInfo;
import io.apiorchestrationservice.api.business.dto.outport.SeatCreateInfo;
import io.apiorchestrationservice.api.business.dto.outport.TemporaryReservationCreateInfo;
import io.apiorchestrationservice.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */
public record ReservationCreateResponse(
	Long temporaryReservationId,
	Long userId,
	ConcertOptionCreateInfo concertOption,
	SeatCreateInfo seat,
	Boolean isConfirmed,
	LocalDateTime reserveAt,
	LocalDateTime createdAt,
	LocalDateTime requestAt
) {


	public static ReservationCreateResponse from(TemporaryReservationCreateInfo info) {
		return ObjectMapperBasedVoMapper.convert(info, ReservationCreateResponse.class);
	}
}
