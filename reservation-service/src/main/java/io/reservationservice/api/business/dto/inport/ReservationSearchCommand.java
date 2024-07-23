package io.reservationservice.api.business.dto.inport;

import java.time.LocalDateTime;

import io.reservationservice.api.business.domainentity.ConcertOption;
import io.reservationservice.api.business.domainentity.Seat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationSearchCommand implements DateSearchCommand{
	private Long reservationId;
	private Long userId;
	private ConcertOption concertOption;
	private Long concertOptionId;
	private Seat seat;
	private Long seatId;
	private Boolean isCanceled;
	private LocalDateTime temporalReservationReserveAt;
	private LocalDateTime reserveAt;
	private LocalDateTime createdAt;
	private LocalDateTime requestAt;

	private DateSearchCondition dateSearchCondition;

	public static ReservationSearchCommand userIdAndConcertOptionId(Long userId, Long concertOptionId) {
		return ReservationSearchCommand.builder().userId(userId).concertOptionId(concertOptionId).build();
	}
}
