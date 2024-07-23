package io.reservationservice.api.application.dto.response;

import java.time.LocalDateTime;

import io.reservationservice.api.business.dto.outport.TemporalReservationCreateInfo;
import io.reservationservice.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
public record TemporalReservationCreateResponse(
	Long temporalReservationId,
	Long userId,
	ConcertOptionCreateResponse concertOption,
	SeatCreateResponse seat,
	Boolean isConfirmed,
	LocalDateTime reserveAt,
	LocalDateTime createdAt,
	LocalDateTime requestAt
) {
	public static TemporalReservationCreateResponse from(TemporalReservationCreateInfo info) {
		return ObjectMapperBasedVoMapper.convert(info, TemporalReservationCreateResponse.class);
	}
}
