package io.apiorchestrationservice.api.application.dto.request;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BalanceChargeRequest {
	private Long userId;
	private BigDecimal amount;
}
