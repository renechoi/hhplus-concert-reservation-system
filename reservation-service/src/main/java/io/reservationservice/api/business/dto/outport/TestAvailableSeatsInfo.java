package io.reservationservice.api.business.dto.outport;

import java.time.LocalDateTime;

import io.reservationservice.api.business.domainentity.ConcertOption;
import io.reservationservice.api.business.domainentity.Seat;
import io.reservationservice.common.mapper.ObjectMapperBasedVoMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestAvailableSeatsInfo{
	 Long seatId;
	 ConcertOption concertOption;
	 Long seatNumber;
	 Boolean occupied;
	 LocalDateTime createdAt;


	public static TestAvailableSeatsInfo from(Seat seat) {
		return ObjectMapperBasedVoMapper.convert(seat, TestAvailableSeatsInfo.class);
	}
}
