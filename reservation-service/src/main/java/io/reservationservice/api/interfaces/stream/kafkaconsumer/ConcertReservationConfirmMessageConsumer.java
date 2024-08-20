package io.reservationservice.api.interfaces.stream.kafkaconsumer;

import static io.reservationservice.common.mapper.ObjectMapperBasedVoMapper.*;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.reservationservice.api.interfaces.stream.payload.ConcertReservationConfirmMessagePayload;
import io.reservationservice.common.annotation.KafkaConsumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : Rene Choi
 * @since : 2024/07/11
 */
@Component
@RequiredArgsConstructor
@KafkaConsumer
@Slf4j
public class ConcertReservationConfirmMessageConsumer implements KafkaMessageConsumer {

	private final ApplicationEventPublisher applicationEventPublisher;

	@Override
	public void handle(Message<String> message, Acknowledgment acknowledgment) {
		log.info("Received message: {}", message.getPayload());
		try {
			ConcertReservationConfirmMessagePayload payload = getObjectMapper().readValue(message.getPayload(), ConcertReservationConfirmMessagePayload.class);
			applicationEventPublisher.publishEvent(payload.withOutboxEventId(parseOutboxEventId(message)));
			acknowledgment.acknowledge();
		} catch (Exception e) {
			log.error("Error processing Kafka message", e);
		}
	}

	private static Long parseOutboxEventId(Message<String> message) {
		return message.getHeaders().get("outbox-event-id", Long.class);
	}
}
