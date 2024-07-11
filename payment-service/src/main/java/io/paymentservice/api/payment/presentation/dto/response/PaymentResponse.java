package io.paymentservice.api.payment.presentation.dto.response;

import java.math.BigDecimal;

import io.paymentservice.api.payment.business.domainentity.PaymentStatus;
import io.paymentservice.api.payment.business.dto.outport.PaymentInfo;
import io.paymentservice.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/07/10
 */
public record PaymentResponse(
	Long transactionId,
	Long userId,
	BigDecimal amount,
	PaymentStatus paymentStatus
) {
	public static PaymentResponse from(PaymentInfo paymentInfo) {
		return ObjectMapperBasedVoMapper.convert(paymentInfo, PaymentResponse.class);
	}
}
