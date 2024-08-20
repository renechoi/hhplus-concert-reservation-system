package io.reservationservice.api.business.dto.inport;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/08/15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OutboxStatusCompleteCommand {
	private Long outboxEventId;
	private Long reservationId;
	private Long userId;
	private Long concertOptionId;
	private String seatNumber;
	private String topic;
}
