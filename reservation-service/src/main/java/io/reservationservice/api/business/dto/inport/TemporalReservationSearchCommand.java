package io.reservationservice.api.business.dto.inport;

import static io.reservationservice.api.business.dto.inport.DateSearchCommand.DateSearchCondition.*;
import static io.reservationservice.api.business.dto.inport.DateSearchCommand.DateSearchTarget.*;

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
public class TemporalReservationSearchCommand implements DateSearchCommand {
	private Long temporalReservationId;
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

	private DateSearchCondition dateSearchCondition;
	private DateSearchTarget dateSearchTarget;

	public static TemporalReservationSearchCommand onMatchingTemporalReservaion(Long userId, Long concertOptionId) {
		return TemporalReservationSearchCommand.builder().userId(userId).concertOptionId(concertOptionId).build();
	}

	public static TemporalReservationSearchCommand expireAt(LocalDateTime searchTime) {
		return TemporalReservationSearchCommand.builder().expireAt(searchTime).dateSearchTarget(EXPIRE_AT).dateSearchCondition(BEFORE).build();

	}
}
