package io.paymentservice.api.balance.business.dto.inport;

import static io.paymentservice.api.balance.business.domainentity.TransactionReason.*;

import java.math.BigDecimal;

import io.paymentservice.api.balance.business.domainentity.TransactionReason;
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
public class BalanceUseCommand {
	private Long userId;
	private BigDecimal amount;
	private TransactionReason transactionReason;

	public static BalanceUseCommand of(Long userId, BigDecimal amount ) {
		return BalanceUseCommand.builder().userId(userId).amount(amount).build();
	}

	public static BalanceUseCommand paymentCommand(Long userId, BigDecimal amount) {
		return BalanceUseCommand.builder().userId(userId).amount(amount).transactionReason(PAYMENT).build();
	}
}
