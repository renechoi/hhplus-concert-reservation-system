package io.reservationservice.api.business.service.impl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/08/15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationConfirmEvent {
	private Long reservationId;
	private Long userId;
	private Long concertOptionId;
	private Long seatNumber;
	private String topic;

}