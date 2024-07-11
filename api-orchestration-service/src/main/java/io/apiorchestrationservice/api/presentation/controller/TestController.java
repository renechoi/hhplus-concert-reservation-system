package io.apiorchestrationservice.api.presentation.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.apiorchestrationservice.api.business.dto.event.PaymentInternalEvent;
import io.apiorchestrationservice.api.infrastructure.stream.internallistener.KafkaMessageProducingInternalEventHandler;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/11
 */
@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
@Tag(name = "Test API", description = "카프카 메시지 producing 테스트 API")
public class TestController {
	private final KafkaMessageProducingInternalEventHandler kafkaMessageProducingInternalEventHandler;

	@RequestMapping("/send")
	public void send(){
		kafkaMessageProducingInternalEventHandler.handlePaymentInternalEvent(PaymentInternalEvent.builder().paymentType("COMPLETED").userId("1").paymentTransactionId("ss").build());
	}

}
