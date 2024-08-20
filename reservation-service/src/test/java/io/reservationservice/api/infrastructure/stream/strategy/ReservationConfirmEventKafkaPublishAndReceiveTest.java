package io.reservationservice.api.infrastructure.stream.strategy;

import static io.reservationservice.common.mapper.ObjectMapperBasedVoMapper.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;

import io.reservationservice.api.business.domainentity.OutboxEvent;
import io.reservationservice.api.business.dto.event.ReservationConfirmEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : Rene Choi
 * @since : 2024/08/15
 */
@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, topics = {"concert-reservation-confirm"})
@Slf4j
public class ReservationConfirmEventKafkaPublishAndReceiveTest {

	@Autowired
	private StreamBridge streamBridge;

	@Autowired
	private EmbeddedKafkaBroker embeddedKafkaBroker;

	private ReservationConfirmEventStrategy reservationConfirmEventStrategy;

	@BeforeEach
	void setUp() {
		reservationConfirmEventStrategy = new ReservationConfirmEventStrategy(streamBridge);
	}

	@Test
	@DisplayName("Reservation Confirm 이벤트 발행 및 수신 테스트")
	void testReservationConfirmEventPublishing() throws Exception {
		// Given
		ReservationConfirmEvent event = ReservationConfirmEvent.builder()
			.reservationId(1L)
			.userId(1L)
			.concertOptionId(1L)
			.seatNumber("A1")
			.topic("concert-reservation-confirm")
			.build();

		String payload = getObjectMapper().writeValueAsString(event);

		OutboxEvent outboxEvent = OutboxEvent.builder()
			.eventType("ReservationConfirmEvent")
			.payload(payload)
			.topic("concert-reservation-confirm")
			.build();

		// When: ReservationConfirmEventStrategy를 통해 이벤트를 Kafka에 게시
		reservationConfirmEventStrategy.process(outboxEvent);

		// Then: Kafka로부터 이벤트를 소비하고, 게시된 이벤트와 동일한지 검증

		// Kafka 소비자 설정을 초기화하고, 주제를 "concert-reservation-confirm"으로 설정
		Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("testGroup", "true", embeddedKafkaBroker);
		consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		consumerProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");

		// Kafka 소비자 팩토리를 생성하여 소비자를 만드는 부분
		DefaultKafkaConsumerFactory<String, String> consumerFactory = new DefaultKafkaConsumerFactory<>(consumerProps, new StringDeserializer(), new StringDeserializer());
		var consumer = consumerFactory.createConsumer();
		consumer.subscribe(java.util.List.of("concert-reservation-confirm"));

		// Kafka로부터 단일 레코드를 가져오는 부분
		ConsumerRecord<String, String> received = KafkaTestUtils.getSingleRecord(consumer, "concert-reservation-confirm");

		// 받은 메시지를 ReservationConfirmEvent 객체로 역직렬화하고, 원래 이벤트와 비교하여 동일함을 확인하는 부분
		ReservationConfirmEvent receivedEvent = getObjectMapper().readValue(received.value(), ReservationConfirmEvent.class);
		assertThat(receivedEvent).isEqualToComparingFieldByField(event);

		consumer.close();
	}
}

