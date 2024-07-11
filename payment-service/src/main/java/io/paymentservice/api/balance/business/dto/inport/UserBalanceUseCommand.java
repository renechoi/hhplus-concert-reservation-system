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
public class UserBalanceUseCommand {
	private Long userId;
	private BigDecimal amount;
	private TransactionReason transactionReason;

	public static UserBalanceUseCommand of(Long userId, BigDecimal amount ) {
		return UserBalanceUseCommand.builder().userId(userId).amount(amount).build();
	}

	public static UserBalanceUseCommand createPaymentCommand(Long userId, BigDecimal amount) {
		return UserBalanceUseCommand.builder().userId(userId).amount(amount).transactionReason(PAYMENT).build();
	}
}
