package io.apiorchestrationservice.api.application.dto.response;

import java.math.BigDecimal;

import io.apiorchestrationservice.api.business.dto.event.PaymentInternalEvent;
import io.apiorchestrationservice.api.business.dto.outport.PaymentInfo;
import io.apiorchestrationservice.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */
public record PaymentResponse(
	Long transactionId,
	Long userId,
	BigDecimal amount,
	String paymentStatus
) {



	public static PaymentResponse from(PaymentInfo paymentInfo) {
		return ObjectMapperBasedVoMapper.convert(paymentInfo, PaymentResponse.class);
	}

	public static PaymentResponse createRolledBackResponse(PaymentResponse paymentResponse) {
		return new PaymentResponse(null, paymentResponse.userId(), null, null);
	}

	public PaymentInternalEvent toPaymentInternalEventAsComplete() {
		return PaymentInternalEvent.builder().paymentTransactionId(this.transactionId().toString()).userId(this.userId().toString()).paymentType("COMPLETED").build();
	}
}