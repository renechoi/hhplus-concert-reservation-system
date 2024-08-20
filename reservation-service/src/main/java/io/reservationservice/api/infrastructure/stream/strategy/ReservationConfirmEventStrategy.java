package io.reservationservice.api.infrastructure.stream.strategy;

import static io.reservationservice.common.mapper.ObjectMapperBasedVoMapper.*;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import io.reservationservice.api.business.domainentity.OutboxEvent;
import io.reservationservice.api.business.dto.event.ReservationConfirmEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : Rene Choi
 * @since : 2024/08/11
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ReservationConfirmEventStrategy implements OutboxEventStrategy {
	private final StreamBridge streamBridge;


	@Override
	public boolean supports(String eventType) {
		return "ReservationConfirmEvent".equals(eventType);
	}

	@Override
	public void process(OutboxEvent outboxEvent) {
		try {
			ReservationConfirmEvent event = convertPayload(outboxEvent);
			streamBridge.send(outboxEvent.getTopic(), createMessagePayload(event).build());

			log.info("Published ReservationConfirmEvent to Kafka: {}", event);
		} catch (Exception e) {
			log.error("Error publishing ReservationConfirmEvent to Kafka", e);
		}
	}

	private ReservationConfirmEvent convertPayload(OutboxEvent outboxEvent) throws JsonProcessingException {
		// JSON 페이로드를 JsonNode로 파싱
		JsonNode payloadNode = getObjectMapper().readTree(outboxEvent.getPayload());

		// 필요한 필드를 추출하여 ReservationConfirmEvent 객체 생성
		Long concertOptionId = payloadNode.path("concert_option").path("concert_option_id").asLong();
		String seatNumber = payloadNode.path("seat").path("seat_number").asText();

		// ReservationConfirmEvent 생성
		return ReservationConfirmEvent.builder()
			.outboxEventId(outboxEvent.getOutboxEventId())
			.reservationId(payloadNode.path("reservation_id").asLong())
			.userId(payloadNode.path("user_id").asLong())
			.concertOptionId(concertOptionId)
			.seatNumber(seatNumber)
			.topic(outboxEvent.getTopic())
			.build();
	}

	private MessageBuilder<String> createMessagePayload(ReservationConfirmEvent event) throws JsonProcessingException {
		return MessageBuilder.withPayload(getObjectMapper().writeValueAsString(event))
			.setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
			.setHeader("event-type", "ReservationConfirmEvent")
			.setHeader("outbox-event-id", event.getOutboxEventId());
	}
}
