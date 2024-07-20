package io.paymentservice.api.balance.interfaces.dto.request;

import java.math.BigDecimal;

import io.paymentservice.api.balance.business.dto.inport.UserBalanceUseCommand;
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
public class UserBalanceUseRequest {
	@NotNull
	private Long userId;

	@NotNull
	private BigDecimal amount;

	public UserBalanceUseRequest withUserId(long userId) {
		this.userId = userId;
		return this;
	}

	public UserBalanceUseCommand toCommand() {
		return ObjectMapperBasedVoMapper.convert(this, UserBalanceUseCommand.class);
	}
}
