package io.reservationservice.api.application.dto.request;

import io.reservationservice.api.business.dto.inport.OutboxStatusCompleteCommand;
import io.reservationservice.common.mapper.ObjectMapperBasedVoMapper;
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
public class OutboxStatusCompleteRequest {
	private Long outboxEventId;
	private Long reservationId;
	private Long userId;
	private Long concertOptionId;
	private String seatNumber;
	private String topic;

	public OutboxStatusCompleteCommand toCommand() {
		return ObjectMapperBasedVoMapper.convert(this, OutboxStatusCompleteCommand.class);

	}
}
