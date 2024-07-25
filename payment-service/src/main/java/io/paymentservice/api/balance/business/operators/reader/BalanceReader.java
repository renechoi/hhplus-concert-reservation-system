package io.paymentservice.api.balance.business.operators.reader;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.paymentservice.api.balance.business.dto.inport.BalanceChargeCommand;
import io.paymentservice.api.balance.business.dto.outport.BalanceSearchInfo;
import io.paymentservice.api.balance.business.persistence.BalanceRepository;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/10
 */

@Component
@RequiredArgsConstructor
public class BalanceReader {

	private final BalanceRepository balanceRepository;

	@Transactional(readOnly = true)
	public BalanceSearchInfo search(BalanceChargeCommand command) {
		return BalanceSearchInfo.from(balanceRepository.getBalance(command.getUserId()));
	}
}
