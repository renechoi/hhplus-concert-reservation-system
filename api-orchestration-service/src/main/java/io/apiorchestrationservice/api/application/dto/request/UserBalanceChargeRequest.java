package io.apiorchestrationservice.api.application.dto.request;

import java.math.BigDecimal;

import io.apiorchestrationservice.api.business.dto.inport.UserBalanceChargeCommand;
import io.apiorchestrationservice.common.mapper.ObjectMapperBasedVoMapper;
import jakarta.validation.constraints.NotNull;
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
public class UserBalanceChargeRequest {
	@NotNull
	private Long userId;
	@NotNull
	private BigDecimal amount;

	public UserBalanceChargeCommand toCommand() {
		return ObjectMapperBasedVoMapper.convert(this, UserBalanceChargeCommand.class);
	}
}
