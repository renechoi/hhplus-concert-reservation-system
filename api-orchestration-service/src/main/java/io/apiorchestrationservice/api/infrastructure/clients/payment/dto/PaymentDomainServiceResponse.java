package io.apiorchestrationservice.api.infrastructure.clients.payment.dto;

import java.math.BigDecimal;

import io.apiorchestrationservice.api.business.dto.outport.PaymentInfo;
import io.apiorchestrationservice.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/07/10
 */
public record PaymentDomainServiceResponse(
    Long transactionId,
    Long userId,
    BigDecimal amount,
    String paymentStatus
) {
	public PaymentInfo toPaymentInfo() {
		return ObjectMapperBasedVoMapper.convert(this, PaymentInfo.class);
	}
}
