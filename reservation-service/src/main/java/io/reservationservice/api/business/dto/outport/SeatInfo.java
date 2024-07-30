package io.reservationservice.api.business.dto.outport;

import java.io.Serializable;
import java.time.LocalDateTime;

import io.reservationservice.api.business.domainentity.Seat;
import io.reservationservice.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/07/08
 */
public record SeatInfo(
	 Long seatId,
	 ConcertOptionInfo concertOption,
	 String section,
	 String seatRow,
	 String seatNumber,
	 Boolean occupied,
	 LocalDateTime createdAt
) implements Serializable {
	public static SeatInfo from(Seat seat) {
		return ObjectMapperBasedVoMapper.convert(seat, SeatInfo.class);
	}
}
