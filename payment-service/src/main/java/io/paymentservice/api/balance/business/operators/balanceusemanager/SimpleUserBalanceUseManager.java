package io.paymentservice.api.balance.business.operators.balanceusemanager;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.paymentservice.api.balance.business.domainentity.UserBalance;
import io.paymentservice.api.balance.business.dto.inport.UserBalanceUseCommand;
import io.paymentservice.api.balance.business.dto.outport.UserBalanceUseInfo;
import io.paymentservice.api.balance.business.persistence.UserBalanceRepository;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/10
 */

@Component
@RequiredArgsConstructor
public class SimpleUserBalanceUseManager implements UserBalanceUseManager{

	private final UserBalanceRepository userBalanceRepository;

	@Override
	@Transactional
	public UserBalanceUseInfo use(UserBalanceUseCommand command) {
		UserBalance userBalance = userBalanceRepository.findByUserIdWithThrows(command.getUserId());
		userBalance.deductAmount(command.getAmount(), command.getTransactionReason());
		return UserBalanceUseInfo.from(userBalanceRepository.save(userBalance));
	}
}
