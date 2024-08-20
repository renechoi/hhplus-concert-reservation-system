package io.reservationservice.api.interfaces.stream.payload;

import io.reservationservice.api.application.dto.request.OutboxStatusCompleteRequest;
import io.reservationservice.common.mapper.ObjectMapperBasedVoMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConcertReservationConfirmMessagePayload {
	private Long outboxEventId;
	private Long reservationId;
	private Long userId;
	private Long concertOptionId;
	private String seatNumber;
	private String topic;

	public OutboxStatusCompleteRequest toOutboxStatusCompleteRequest() {
		return ObjectMapperBasedVoMapper.convert(this, OutboxStatusCompleteRequest.class);
	}

	public ConcertReservationConfirmMessagePayload withOutboxEventId(Long outboxEventId) {
		this.outboxEventId = outboxEventId;
		return this;
	}
}
