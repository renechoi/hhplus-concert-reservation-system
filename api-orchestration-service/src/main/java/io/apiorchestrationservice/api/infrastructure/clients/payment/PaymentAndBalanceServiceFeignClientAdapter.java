package io.apiorchestrationservice.api.infrastructure.clients.payment;

import java.util.Optional;

import org.springframework.http.ResponseEntity;

import io.apiorchestrationservice.api.business.client.PaymentServiceClientAdapter;
import io.apiorchestrationservice.api.business.dto.inport.PaymentProcessCommand;
import io.apiorchestrationservice.api.business.dto.inport.UserBalanceChargeCommand;
import io.apiorchestrationservice.api.business.dto.outport.BalanceChargeInfo;
import io.apiorchestrationservice.api.business.dto.outport.BalanceSearchInfo;
import io.apiorchestrationservice.api.business.dto.outport.PaymentHistoryInfos;
import io.apiorchestrationservice.api.business.dto.outport.PaymentInfo;
import io.apiorchestrationservice.api.infrastructure.clients.payment.dto.BalanceTransactionDomainServiceResponses;
import io.apiorchestrationservice.api.infrastructure.clients.payment.dto.PaymentDomainServiceRequest;
import io.apiorchestrationservice.api.infrastructure.clients.payment.dto.PaymentDomainServiceResponse;
import io.apiorchestrationservice.api.infrastructure.clients.payment.dto.PaymentHistoryDomainServiceResponses;
import io.apiorchestrationservice.api.infrastructure.clients.payment.dto.UserBalanceChargeDomainServiceRequest;
import io.apiorchestrationservice.api.infrastructure.clients.payment.dto.UserBalanceChargeDomainServiceResponse;
import io.apiorchestrationservice.api.infrastructure.clients.payment.dto.UserBalanceSearchDomainServiceResponse;
import io.apiorchestrationservice.api.infrastructure.clients.payment.dto.UserBalanceUseDomainServiceRequest;
import io.apiorchestrationservice.api.infrastructure.clients.payment.dto.UserBalanceUseDomainServiceResponse;
import io.apiorchestrationservice.api.infrastructure.clients.validator.FeignResponseValidator;
import io.apiorchestrationservice.common.annotation.FeignAdapter;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */
@FeignAdapter
@RequiredArgsConstructor
public class PaymentAndBalanceServiceFeignClientAdapter implements PaymentServiceClientAdapter {
	private final PaymentAndBalanceServiceClient paymentAndBalanceServiceClient;

	private final FeignResponseValidator responseValidator;

	@Override
	public PaymentInfo processPayment(PaymentProcessCommand command) {
		ResponseEntity<PaymentDomainServiceResponse> responseEntity = paymentAndBalanceServiceClient.processPayment(PaymentDomainServiceRequest.from(command));
		responseValidator.validate(responseEntity);
		return Optional.ofNullable(responseEntity.getBody()).map(PaymentDomainServiceResponse::toPaymentInfo).orElseThrow();
	}

	@Override
	public PaymentInfo cancelPayment(Long transactionId) {
		ResponseEntity<PaymentDomainServiceResponse> responseEntity = paymentAndBalanceServiceClient.cancelPayment(transactionId);
		responseValidator.validate(responseEntity);
		return Optional.ofNullable(responseEntity.getBody()).map(PaymentDomainServiceResponse::toPaymentInfo).orElseThrow();
	}

	@Override
	public PaymentHistoryInfos retrievePaymentHistory(Long userId) {
		ResponseEntity<PaymentHistoryDomainServiceResponses> responseEntity = paymentAndBalanceServiceClient.retrievePaymentHistory(userId);
		responseValidator.validate(responseEntity);
		return Optional.ofNullable(responseEntity.getBody()).map(PaymentHistoryDomainServiceResponses::toPaymentHistoryInfo).orElseThrow();
	}







	@Override
	public BalanceSearchInfo search(long userId) {
		ResponseEntity<UserBalanceSearchDomainServiceResponse> responseEntity = paymentAndBalanceServiceClient.balanceSearch(userId);
		responseValidator.validate(responseEntity);
		return Optional.ofNullable(responseEntity.getBody()).map(UserBalanceSearchDomainServiceResponse::toBalanceSearchInfo).orElseThrow();
	}

	@Override
	public BalanceTransactionDomainServiceResponses history(long userId) {
		ResponseEntity<BalanceTransactionDomainServiceResponses> responseEntity = paymentAndBalanceServiceClient.history(userId);
		responseValidator.validate(responseEntity);
		return responseEntity.getBody();
	}

	@Override
	public BalanceChargeInfo charge(long userId, UserBalanceChargeCommand command) {
		ResponseEntity<UserBalanceChargeDomainServiceResponse> responseEntity = paymentAndBalanceServiceClient.charge(userId, UserBalanceChargeDomainServiceRequest.from(command));
		responseValidator.validate(responseEntity);
		return Optional.ofNullable(responseEntity.getBody()).map(UserBalanceChargeDomainServiceResponse::toBalanceChargeInfo).orElseThrow();
	}

	@Override
	public UserBalanceUseDomainServiceResponse use(long userId, UserBalanceChargeCommand command) {
		ResponseEntity<UserBalanceUseDomainServiceResponse> responseEntity = paymentAndBalanceServiceClient.use(userId, UserBalanceUseDomainServiceRequest.from(command));
		responseValidator.validate(responseEntity);
		return responseEntity.getBody();
	}

}
