package io.paymentservice.api.balance.business.operators.balancecharger;

import static io.paymentservice.api.balance.business.entity.Balance.*;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.paymentservice.api.balance.business.dto.inport.BalanceChargeCommand;
import io.paymentservice.api.balance.business.dto.outport.BalanceChargeInfo;
import io.paymentservice.api.balance.business.persistence.BalanceRepository;
import io.paymentservice.common.exception.definitions.BalanceChargeUnAvailableException;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/10
 */
@Component
@RequiredArgsConstructor
public class BalanceCharger {

	private final BalanceRepository balanceRepository;

	@Transactional
	public BalanceChargeInfo charge(BalanceChargeCommand command) {
		return balanceRepository.findByUserIdOptional(command.getUserId())
			.or(() -> Optional.of(createDefaultNewBalance(command.getUserId())))
			.map(balance -> balance.charge(command.getAmount(), command.getTransactionReason()))
			.map(balanceRepository::save)
			.map(BalanceChargeInfo::from)
			.orElseThrow(BalanceChargeUnAvailableException::new);
	}
}

