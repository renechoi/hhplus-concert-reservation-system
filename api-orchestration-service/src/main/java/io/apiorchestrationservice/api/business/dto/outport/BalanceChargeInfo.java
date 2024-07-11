package io.apiorchestrationservice.api.business.dto.outport;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author : Rene Choi
 * @since : 2024/07/10
 */
public record BalanceChargeInfo(
	Long userBalanceId,
	Long userId,
	BigDecimal amount,
	LocalDateTime createdAt,
	LocalDateTime updatedAt
) {

}
