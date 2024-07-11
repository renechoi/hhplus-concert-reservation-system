package io.apiorchestrationservice.api.business.dto.outport;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author : Rene Choi
 * @since : 2024/07/10
 */
public record PaymentHistoryInfo(
	Long transactionId,
	Long userId,
	BigDecimal amount,
	String paymentStatus,
	LocalDateTime createdAt
) {
}
