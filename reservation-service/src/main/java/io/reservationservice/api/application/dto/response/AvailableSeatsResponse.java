package io.reservationservice.api.application.dto.response;

import java.time.LocalDateTime;

import io.reservationservice.api.business.domainentity.ConcertOption;
import io.reservationservice.api.business.dto.outport.AvailableSeatsInfo;
import io.reservationservice.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/07/08
 */
public record AvailableSeatsResponse(
	Long seatId,
	ConcertOption concertOption,
	Long seatNumber,
	Boolean occupied,
	LocalDateTime createdAt
) {
	public static AvailableSeatsResponse from(AvailableSeatsInfo info) {
		return ObjectMapperBasedVoMapper.convert(info, AvailableSeatsResponse.class);
	}
}
