package io.reservationservice.api.application.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.reservationservice.api.business.dto.outport.SeatInfo;
import io.reservationservice.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/07/08
 */
public record SeatResponse(
	Long seatId,
	@JsonIgnore
	ConcertOptionResponse concertOption,
	String section,
	String seatRow,
	String seatNumber,
	Boolean occupied,
	LocalDateTime createdAt
) {
	public static SeatResponse from(SeatInfo info) {
		return ObjectMapperBasedVoMapper.convert(info, SeatResponse.class);
	}
}
