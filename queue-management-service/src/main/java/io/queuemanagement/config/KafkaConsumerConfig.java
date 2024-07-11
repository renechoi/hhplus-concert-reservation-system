package io.queuemanagement.config;

import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;

import io.queuemanagement.api.infrastructure.stream.kafkaconsumer.ConcertReservationPaymentMessageConsumer;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/11
 */
@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig {

	private final ConcertReservationPaymentMessageConsumer concertReservationPaymentMessageConsumer;

	@Bean
	public Consumer<Message<String>> concertReservationPaymentConsumer(){
		return message-> concertReservationPaymentMessageConsumer.handle(message, message.getHeaders().get(KafkaHeaders.ACKNOWLEDGMENT, Acknowledgment.class));
	}

}
