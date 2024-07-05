package io.apiorchestrationservice.api.application.dto.response;

import java.math.BigDecimal;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */
public record BalanceResponse(
	Long userId,
	BigDecimal amount
) {
}