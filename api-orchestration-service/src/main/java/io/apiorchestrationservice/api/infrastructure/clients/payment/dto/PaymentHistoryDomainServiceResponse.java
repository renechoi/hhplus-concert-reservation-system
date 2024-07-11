package io.apiorchestrationservice.api.infrastructure.clients.payment.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import io.apiorchestrationservice.api.business.dto.outport.PaymentHistoryInfo;
import io.apiorchestrationservice.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/07/10
 */
public record PaymentHistoryDomainServiceResponse(
    Long transactionId,
    Long userId,
    BigDecimal amount,
    String paymentStatus,
    LocalDateTime createdAt
) {
	public PaymentHistoryInfo toPaymentHistoryInfo() {
		return ObjectMapperBasedVoMapper.convert(this, PaymentHistoryInfo.class);
	}
}
