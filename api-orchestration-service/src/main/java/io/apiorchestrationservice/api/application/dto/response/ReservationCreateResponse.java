package io.apiorchestrationservice.api.application.dto.response;

import java.time.LocalDateTime;

import io.apiorchestrationservice.api.business.dto.outport.ConcertOptionCreateInfo;
import io.apiorchestrationservice.api.business.dto.outport.SeatCreateInfo;
import io.apiorchestrationservice.api.business.dto.outport.TemporalReservationCreateInfo;
import io.apiorchestrationservice.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */
public record ReservationCreateResponse(
	Long temporalReservationId,
	Long userId,
	ConcertOptionCreateInfo concertOption,
	SeatCreateInfo seat,
	Boolean isConfirmed,
	LocalDateTime reserveAt,
	LocalDateTime createdAt,
	LocalDateTime requestAt
) {


	public static ReservationCreateResponse from(TemporalReservationCreateInfo info) {
		return ObjectMapperBasedVoMapper.convert(info, ReservationCreateResponse.class);
	}
}
