package io.paymentservice.api.balance.business.operators.reader;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.paymentservice.api.balance.business.dto.inport.UserBalanceChargeCommand;
import io.paymentservice.api.balance.business.dto.outport.UserBalanceSearchInfo;
import io.paymentservice.api.balance.business.persistence.UserBalanceRepository;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/10
 */

@Component
@RequiredArgsConstructor
public class UserBalanceReader {

	private final UserBalanceRepository userBalanceRepository;

	@Transactional(readOnly = true)
	public UserBalanceSearchInfo search(UserBalanceChargeCommand command) {
		return UserBalanceSearchInfo.from(userBalanceRepository.findByUserIdWithThrows(command.getUserId()));
	}
}
