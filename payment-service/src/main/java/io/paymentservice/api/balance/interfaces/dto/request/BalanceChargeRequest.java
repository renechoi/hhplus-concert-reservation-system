package io.paymentservice.api.balance.interfaces.dto.request;

import java.math.BigDecimal;

import io.paymentservice.api.balance.business.dto.inport.BalanceChargeCommand;
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
public class BalanceChargeRequest {

	@NotNull
	private Long userId;

	@NotNull
	private BigDecimal amount;


	public BalanceChargeRequest withUserId(long userId) {
		this.userId= userId;
		return this;
	}

	public BalanceChargeCommand toCommand() {

		return ObjectMapperBasedVoMapper.convert(this, BalanceChargeCommand.class);
	}
}
