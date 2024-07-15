package io.apiorchestrationservice.api.interfaces.presentation.controller;

import static io.apiorchestrationservice.common.model.CommonApiResponse.*;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.apiorchestrationservice.api.application.dto.request.PaymentProcessRequest;
import io.apiorchestrationservice.api.application.dto.request.UserBalanceChargeRequest;
import io.apiorchestrationservice.api.application.dto.response.PaymentResponse;
import io.apiorchestrationservice.api.application.dto.response.UserBalanceChargeResponse;
import io.apiorchestrationservice.api.application.dto.response.UserBalanceSearchResponse;
import io.apiorchestrationservice.api.application.facade.BalanceChargeFacade;
import io.apiorchestrationservice.api.application.facade.BalanceCrudFacade;
import io.apiorchestrationservice.api.application.facade.PaymentFacade;
import io.apiorchestrationservice.common.annotation.ValidatedToken;
import io.apiorchestrationservice.common.model.CommonApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */
@RestController
@RequestMapping("/api/user-balance")
@RequiredArgsConstructor
public class BalanceAndPaymentOrchestrationController {
	private final BalanceChargeFacade balanceChargeFacade;
	private final BalanceCrudFacade balanceCrudFacade;
	private final PaymentFacade paymentFacade;


	@PostMapping("/charge")
	@Operation(summary = "잔액 충전 API")
	public CommonApiResponse<UserBalanceChargeResponse> chargeBalance(@RequestBody @Validated UserBalanceChargeRequest request) {
		return OK(balanceChargeFacade.charge(request));
	}

	@Operation(summary = "잔액 조회 API")
	@GetMapping("/{userId}")
	public CommonApiResponse<UserBalanceSearchResponse> getBalance(@PathVariable Long userId) {
		return OK(balanceCrudFacade.retrieveBalance(userId));
	}

	@PostMapping("/payment")
	@Operation(summary = "결제 처리 API")
	@ValidatedToken
	public CommonApiResponse<PaymentResponse> processPayment(@RequestBody @Validated PaymentProcessRequest request) {
		return OK(paymentFacade.processPayment(request));
	}
}