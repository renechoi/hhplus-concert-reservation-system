package io.apiorchestrationservice.api.business.client;

import io.apiorchestrationservice.api.business.dto.inport.UserBalanceChargeCommand;
import io.apiorchestrationservice.api.business.dto.outport.BalanceChargeInfo;
import io.apiorchestrationservice.api.business.dto.outport.BalanceSearchInfo;
import io.apiorchestrationservice.api.infrastructure.clients.payment.dto.BalanceTransactionDomainServiceResponses;
import io.apiorchestrationservice.api.infrastructure.clients.payment.dto.UserBalanceUseDomainServiceResponse;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */
public interface UserBalanceServiceClientAdapter {
	BalanceSearchInfo search(long userId);

	BalanceTransactionDomainServiceResponses history(long userId);

	BalanceChargeInfo charge(long userId, UserBalanceChargeCommand request);

	UserBalanceUseDomainServiceResponse use(long userId, UserBalanceChargeCommand request);
}
