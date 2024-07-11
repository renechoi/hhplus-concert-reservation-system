package io.apiorchestrationservice.api.business.dto.inport;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBalanceChargeCommand {
	@NotNull
	private Long userId;
	@NotNull
	private BigDecimal amount;
}
