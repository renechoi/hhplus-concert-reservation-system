package io.paymentservice.api.balance.business.operators.balancecharger;

import static io.paymentservice.api.balance.business.domainentity.UserBalance.*;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.paymentservice.api.balance.business.dto.inport.UserBalanceChargeCommand;
import io.paymentservice.api.balance.business.dto.outport.UserBalanceChargeInfo;
import io.paymentservice.api.balance.business.persistence.UserBalanceRepository;
import io.paymentservice.common.exception.UserBalanceChargeUnAvailableException;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/10
 */
@Component
@RequiredArgsConstructor
public class UserBalanceCharger {

	private final UserBalanceRepository userBalanceRepository;

	@Transactional
	public UserBalanceChargeInfo charge(UserBalanceChargeCommand command) {
		return userBalanceRepository.findByUserIdOptional(command.getUserId())
			.or(() -> Optional.of(createDefaultNewUserBalance(command.getUserId())))
			.map(userBalance -> userBalance.addAmount(command.getAmount(), command.getTransactionReason()))
			.map(userBalanceRepository::save)
			.map(UserBalanceChargeInfo::from)
			.orElseThrow(UserBalanceChargeUnAvailableException::new);
	}
}

