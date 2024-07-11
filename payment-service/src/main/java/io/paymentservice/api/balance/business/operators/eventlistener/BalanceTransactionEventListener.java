package io.paymentservice.api.balance.business.operators.eventlistener;

import static org.springframework.transaction.event.TransactionPhase.*;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import io.paymentservice.api.balance.business.domainentity.BalanceTransaction;
import io.paymentservice.api.balance.business.domainentity.TransactionType;
import io.paymentservice.api.balance.business.dto.event.UserBalanceChargeEvent;
import io.paymentservice.api.balance.business.dto.event.UserBalanceUseEvent;
import io.paymentservice.api.balance.business.persistence.BalanceTransactionRepository;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/09
 */
@Component
@RequiredArgsConstructor
public class BalanceTransactionEventListener {

	private final BalanceTransactionRepository transactionRepository;

	@TransactionalEventListener(phase = BEFORE_COMMIT)
	public void handleBalanceChargedEvent(UserBalanceChargeEvent event) {
		BalanceTransaction transaction = BalanceTransaction.builder()
			.userId(event.userId())
			.amount(event.amount())
			.transactionType(TransactionType.CHARGE)
			.transactionReason(event.transactionReason())
			.build();
		transactionRepository.save(transaction);
	}

	@TransactionalEventListener(phase = BEFORE_COMMIT)
	public void handleBalanceUsedEvent(UserBalanceUseEvent event) {
		BalanceTransaction transaction = BalanceTransaction.builder()
			.userId(event.userId())
			.amount(event.amount())
			.transactionType(TransactionType.USE)
			.transactionReason(event.transactionReason())
			.build();
		transactionRepository.save(transaction);
	}
}