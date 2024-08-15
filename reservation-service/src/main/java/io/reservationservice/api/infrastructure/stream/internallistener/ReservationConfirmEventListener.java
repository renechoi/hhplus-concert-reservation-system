package io.reservationservice.api.infrastructure.stream.internallistener;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.util.MimeTypeUtils;

import org.jetbrains.annotations.NotNull;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.reservationservice.api.business.domainentity.OutboxEvent;
import io.reservationservice.api.business.dto.event.ReservationConfirmEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import io.reservationservice.api.business.dto.event.ReservationConfirmEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : Rene Choi
 * @since : 2024/08/15
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ReservationConfirmEventListener {

	private final StreamBridge streamBridge;
	private final ObjectMapper objectMapper;

	@TransactionalEventListener
	public void handleReservationConfirmEvent(ReservationConfirmEvent event) {
		try {
			streamBridge.send(event.getTopic(), createMessagePayload(event).build());
			log.info("Published ReservationConfirmEvent to Kafka: {}", event);
		} catch (Exception e) {
			log.error("Error publishing ReservationConfirmEvent to Kafka", e);
		}
	}


	private MessageBuilder<String> createMessagePayload(ReservationConfirmEvent event) throws JsonProcessingException {
		return 	 MessageBuilder.withPayload(objectMapper.writeValueAsString(event))
			.setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
			.setHeader("event-type", "ReservationConfirmEvent");
	}
}
