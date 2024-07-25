package io.paymentservice.api.balance.business.operators.eventlistener;

import static io.paymentservice.api.balance.business.entity.BalanceTransaction.*;
import static org.springframework.transaction.event.TransactionPhase.*;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import io.paymentservice.api.balance.business.entity.BalanceTransaction;
import io.paymentservice.api.balance.business.entity.TransactionType;
import io.paymentservice.api.balance.business.dto.event.BalanceChargeEvent;
import io.paymentservice.api.balance.business.dto.event.BalanceUseEvent;
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
	public void handleBalanceChargedEvent(BalanceChargeEvent event) {
		transactionRepository.save(createChargedEvent(event));
	}

	@TransactionalEventListener(phase = BEFORE_COMMIT)
	public void handleBalanceUsedEvent(BalanceUseEvent event) {
		transactionRepository.save(createUsedEvent(event));
	}
}