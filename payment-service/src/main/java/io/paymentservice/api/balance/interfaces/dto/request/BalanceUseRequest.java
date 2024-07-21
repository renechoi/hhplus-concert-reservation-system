package io.paymentservice.api.balance.interfaces.dto.request;

import java.math.BigDecimal;

import io.paymentservice.api.balance.business.dto.inport.BalanceUseCommand;
import io.paymentservice.common.mapper.ObjectMapperBasedVoMapper;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BalanceUseRequest {
	@NotNull
	private Long userId;

	@NotNull
	private BigDecimal amount;

	public BalanceUseRequest withUserId(long userId) {
		this.userId = userId;
		return this;
	}

	public BalanceUseCommand toCommand() {
		return ObjectMapperBasedVoMapper.convert(this, BalanceUseCommand.class);
	}
}
