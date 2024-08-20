package io.reservationservice.api.interfaces.stream.kafkaconsumer;

import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.Message;

/**
 * @author : Rene Choi
 * @since : 2024/07/11
 */
public interface KafkaMessageConsumer {
	void handle(Message<String> message, Acknowledgment acknowledgment);
}
