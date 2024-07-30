package io.reservationservice.api.business.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeatChangedEvent {
	private  Long concertOptionId;
	private  Long seatId;
}