package io.messageservice.api.interfaces.stream.kafkaconsumer;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.messageservice.api.interfaces.stream.payload.PaymentMessagePayload;
import io.messageservice.common.annotation.KafkaConsumer;
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
public class ConcertReservationPaymentMessageConsumer implements KafkaMessageConsumer {

	private final ObjectMapper objectMapper;
	private final ApplicationEventPublisher applicationEventPublisher;

	@Override
	public void handle(Message<String> message, Acknowledgment acknowledgment) {
		log.info("Received message: {}", message.getPayload());
		try {
			PaymentMessagePayload paymentMessagePayload = objectMapper.readValue(message.getPayload(), PaymentMessagePayload.class);

			String paymentType = message.getHeaders().get("payment-type", String.class);

			if ("COMPLETED".equals(paymentType)) {
				applicationEventPublisher.publishEvent(paymentMessagePayload);
			} else if ("CANCELLED".equals(paymentType)) {
				applicationEventPublisher.publishEvent(paymentMessagePayload);
			}

			acknowledgment.acknowledge();
		} catch (Exception e) {
			log.error("Error processing Kafka message", e);
		}
	}
}
