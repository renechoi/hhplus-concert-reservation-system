package io.reservationservice.config;

import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;

import io.reservationservice.api.interfaces.stream.kafkaconsumer.ConcertReservationConfirmMessageConsumer;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/08/15
 */
@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig {

	private final ConcertReservationConfirmMessageConsumer concertReservationConfirmMessageConsumer;

	@Bean
	public Consumer<Message<String>> concertReservationConfirmConsumer() {
		return message -> concertReservationConfirmMessageConsumer.handle(message, message.getHeaders().get(KafkaHeaders.ACKNOWLEDGMENT, Acknowledgment.class));
	}

}
