package io.apiorchestrationservice.api.infrastructure.clients.payment;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.apiorchestrationservice.api.infrastructure.clients.payment.dto.BalanceTransactionDomainServiceResponses;
import io.apiorchestrationservice.api.infrastructure.clients.payment.dto.PaymentDomainServiceRequest;
import io.apiorchestrationservice.api.infrastructure.clients.payment.dto.PaymentDomainServiceResponse;
import io.apiorchestrationservice.api.infrastructure.clients.payment.dto.PaymentHistoryDomainServiceResponses;
import io.apiorchestrationservice.api.infrastructure.clients.payment.dto.UserBalanceChargeDomainServiceRequest;
import io.apiorchestrationservice.api.infrastructure.clients.payment.dto.UserBalanceChargeDomainServiceResponse;
import io.apiorchestrationservice.api.infrastructure.clients.payment.dto.UserBalanceSearchDomainServiceResponse;
import io.apiorchestrationservice.api.infrastructure.clients.payment.dto.UserBalanceUseDomainServiceRequest;
import io.apiorchestrationservice.api.infrastructure.clients.payment.dto.UserBalanceUseDomainServiceResponse;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */
@FeignClient(
	name = "payment-service"
)
public interface PaymentAndBalanceServiceClient {

	@PostMapping("/payment/api/user-balance/payment")
	ResponseEntity<PaymentDomainServiceResponse> processPayment(@RequestBody PaymentDomainServiceRequest request);

	@PostMapping("/payment/api/user-balance/payment/cancel/{transactionId}")
	ResponseEntity<PaymentDomainServiceResponse> cancelPayment(@PathVariable Long transactionId);


	@GetMapping("/payment/api/payment/history/{userId}")
	ResponseEntity<PaymentHistoryDomainServiceResponses> retrievePaymentHistory(@PathVariable("userId") Long userId);

	@GetMapping("/payment/api/user-balance/{userId}")
	ResponseEntity<UserBalanceSearchDomainServiceResponse> balanceSearch(@PathVariable("userId") long userId);

	@GetMapping("/payment/api/user-balance/histories/{userId}")
	ResponseEntity<BalanceTransactionDomainServiceResponses> history(@PathVariable("userId") long userId);




	@PutMapping("/payment/api/user-balance/charge/{userId}")
	ResponseEntity<UserBalanceChargeDomainServiceResponse> charge(@PathVariable("userId") long userId, @RequestBody UserBalanceChargeDomainServiceRequest request);

	@PatchMapping("/payment/user-balance/use/{userId}")
	ResponseEntity<UserBalanceUseDomainServiceResponse> use(@PathVariable("userId") long userId, @RequestBody UserBalanceUseDomainServiceRequest request);
}
