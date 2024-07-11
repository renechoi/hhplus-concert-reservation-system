package io.apiorchestrationservice.api.business.client;

import io.apiorchestrationservice.api.business.dto.inport.PaymentProcessCommand;
import io.apiorchestrationservice.api.business.dto.inport.UserBalanceChargeCommand;
import io.apiorchestrationservice.api.business.dto.outport.BalanceChargeInfo;
import io.apiorchestrationservice.api.business.dto.outport.BalanceSearchInfo;
import io.apiorchestrationservice.api.business.dto.outport.PaymentHistoryInfos;
import io.apiorchestrationservice.api.business.dto.outport.PaymentInfo;
import io.apiorchestrationservice.api.infrastructure.clients.payment.dto.BalanceTransactionDomainServiceResponses;
import io.apiorchestrationservice.api.infrastructure.clients.payment.dto.UserBalanceUseDomainServiceResponse;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */
public interface PaymentServiceClientAdapter {
	PaymentInfo processPayment(PaymentProcessCommand command);

	PaymentInfo cancelPayment(Long transactionId);

	PaymentHistoryInfos retrievePaymentHistory(Long userId);

	BalanceSearchInfo search(long userId);

	BalanceTransactionDomainServiceResponses history(long userId);

	BalanceChargeInfo charge(long userId, UserBalanceChargeCommand command);

	UserBalanceUseDomainServiceResponse use(long userId, UserBalanceChargeCommand command);
}
