package io.apiorchestrationservice.api.application.dto.request;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeatReservationRequest {
	private Long userId;
	private Long concertOptionId;
	private Long seatId;
	private LocalDate date;
}
