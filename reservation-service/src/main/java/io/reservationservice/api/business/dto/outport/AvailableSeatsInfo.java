package io.reservationservice.api.business.dto.outport;

import java.time.LocalDateTime;

import io.reservationservice.api.business.domainentity.ConcertOption;
import io.reservationservice.api.business.domainentity.Seat;
import io.reservationservice.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/07/08
 */
public record AvailableSeatsInfo(
	 Long seatId,
	 ConcertOption concertOption,
	 Long seatNumber,
	 Boolean occupied,
	 LocalDateTime createdAt

) {
	public static AvailableSeatsInfo from(Seat seat) {
		return ObjectMapperBasedVoMapper.convert(seat, AvailableSeatsInfo.class);
	}
}
