package io.reservationservice.api.business.dto.inport;

import io.reservationservice.api.business.domainentity.ConcertOption;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatSearchCommand {
	private ConcertOption concertOption;
	private Long ConcertOptionId;
	private Long seatNumber;
	private Long userId;



	public static SeatSearchCommand onConcertOptionSeat(ConcertOption concertOption, Long seatNumber) {
		return SeatSearchCommand.builder().concertOption(concertOption).seatNumber(seatNumber).build();
	}


	public static SeatSearchCommand onConcertOption(Long concertOptionId) {
		return SeatSearchCommand.builder().ConcertOptionId(concertOptionId).build();
	}
}
