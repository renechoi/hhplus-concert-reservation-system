package io.apiorchestrationservice.api.application.dto.response;

import java.time.LocalDateTime;

import io.apiorchestrationservice.api.business.dto.outport.AvailableSeatInfo;
import io.apiorchestrationservice.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */

public record AvailableSeatsResponse(
	Long seatId,
	ConcertOptionCreateResponse concertOption,
	Long seatNumber,
	Boolean occupied,
	LocalDateTime createdAt
) {

	public static AvailableSeatsResponse from(AvailableSeatInfo availableSeatInfo) {
		return ObjectMapperBasedVoMapper.convert(availableSeatInfo, AvailableSeatsResponse.class);
	}
}

