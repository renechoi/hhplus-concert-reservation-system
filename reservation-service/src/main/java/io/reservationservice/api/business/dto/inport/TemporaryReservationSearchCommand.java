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
public class TemporaryReservationSearchCommand {
	private Long temporaryReservationId;
	private Long userId;
	private ConcertOption concertOption;
	private Long concertOptionId;
	private Seat seat;
	private Long seatId;
	private Boolean isConfirmed;
	private LocalDateTime reserveAt;
	private LocalDateTime expireAt;
	private LocalDateTime createdAt;
	private LocalDateTime requestAt;

	private String dateSearchTarget;
	private String dateSearchCondition; // "after", "before", "on"

	public static TemporaryReservationSearchCommand searchTemporaryReservationByUserIdAndConcertOptionId(Long userId, Long concertOptionId) {
		return TemporaryReservationSearchCommand.builder().userId(userId).concertOptionId(concertOptionId).build();
	}

	public static TemporaryReservationSearchCommand searchByExpireAt(LocalDateTime searchTime, String dateSearchCondition) {
		return TemporaryReservationSearchCommand.builder().expireAt(searchTime).dateSearchTarget("expireAt").dateSearchCondition(dateSearchCondition).build();
	}
}
