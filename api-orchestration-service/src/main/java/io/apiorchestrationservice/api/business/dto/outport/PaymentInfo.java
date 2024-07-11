package io.apiorchestrationservice.api.business.dto.outport;

import java.math.BigDecimal;

/**
 * @author : Rene Choi
 * @since : 2024/07/10
 */
public record PaymentInfo(
	Long transactionId,
	Long userId,
	BigDecimal amount,
	String paymentStatus
) {
}
