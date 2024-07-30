package io.paymentservice.api.balance.business.operators.balanceusemanager;

import static io.paymentservice.api.balance.business.dto.inport.BalanceSearchCommand.*;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.paymentservice.api.balance.business.dto.inport.BalanceUseCommand;
import io.paymentservice.api.balance.business.dto.outport.BalanceUseInfo;
import io.paymentservice.api.balance.business.entity.Balance;
import io.paymentservice.api.balance.business.persistence.BalanceRepository;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/10
 */

@Component
@RequiredArgsConstructor
public class SimpleBalanceUseManager implements BalanceUseManager {

	private final BalanceRepository balanceRepository;

	@Override
	@Transactional
	public BalanceUseInfo use(BalanceUseCommand command) {
		Balance balance = balanceRepository.findSingleByConditionWithLock(onUser(command));
		balance.use(command.getAmount(), command.getTransactionReason());
		return BalanceUseInfo.from(balanceRepository.save(balance));
	}
}
