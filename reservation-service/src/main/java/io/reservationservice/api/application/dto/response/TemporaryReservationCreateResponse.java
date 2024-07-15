package io.reservationservice.api.application.dto.response;

import java.time.LocalDateTime;

import io.reservationservice.api.business.dto.outport.TemporaryReservationCreateInfo;
import io.reservationservice.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
public record TemporaryReservationCreateResponse(
	Long temporaryReservationId,
	Long userId,
	ConcertOptionCreateResponse concertOption,
	SeatCreateResponse seat,
	Boolean isConfirmed,
	LocalDateTime reserveAt,
	LocalDateTime createdAt,
	LocalDateTime requestAt
) {
	public static TemporaryReservationCreateResponse from(TemporaryReservationCreateInfo info) {
		return ObjectMapperBasedVoMapper.convert(info, TemporaryReservationCreateResponse.class);
	}
}
