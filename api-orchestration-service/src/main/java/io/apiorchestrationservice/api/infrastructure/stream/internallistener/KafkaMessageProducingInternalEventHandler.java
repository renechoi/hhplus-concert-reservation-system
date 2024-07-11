package io.apiorchestrationservice.api.infrastructure.stream.internallistener;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.apiorchestrationservice.api.business.dto.event.PaymentInternalEvent;
import io.apiorchestrationservice.api.infrastructure.stream.dto.PaymentMessagePayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : Rene Choi
 * @since : 2024/07/11
 */

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaMessageProducingInternalEventHandler {
	private final StreamBridge streamBridge;
	private final ObjectMapper objectMapper;

	@EventListener
	public void handlePaymentInternalEvent(PaymentInternalEvent event) {
		try {
			PaymentMessagePayload payload = PaymentMessagePayload.of(event.getPaymentTransactionId(), event.getUserId(), event.getPaymentType());

			MessageBuilder<String> messageBuilder = MessageBuilder.withPayload(objectMapper.writeValueAsString(payload))
					.setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
						.setHeader("payment-type", payload.getPaymentType());
			streamBridge.send("concert-reservation-payment", messageBuilder.build());

			log.info("Published event to Kafka: {}", event);
		} catch (Exception e) {
			log.error("Error publishing event to Kafka", e);
		}
	}
}
