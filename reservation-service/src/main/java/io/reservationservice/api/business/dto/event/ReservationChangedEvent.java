package io.reservationservice.api.business.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationChangedEvent {
	private Long userId;
	private Long concertOptionId;
}