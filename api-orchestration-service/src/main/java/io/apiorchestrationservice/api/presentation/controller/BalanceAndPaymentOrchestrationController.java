package io.apiorchestrationservice.api.presentation.controller;

import static io.apiorchestrationservice.api.application.dto.response.PaymentResponse.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.apiorchestrationservice.api.application.dto.request.BalanceChargeRequest;
import io.apiorchestrationservice.api.application.dto.request.PaymentRequest;
import io.apiorchestrationservice.api.application.dto.response.BalanceResponse;
import io.apiorchestrationservice.api.application.dto.response.PaymentResponse;
import io.apiorchestrationservice.api.application.facade.BalanceChargeFacade;
import io.apiorchestrationservice.api.application.facade.BalanceCrudFacade;
import io.apiorchestrationservice.api.application.facade.PaymentFacade;
import io.apiorchestrationservice.api.business.domainmodel.PaymentStatus;
import io.apiorchestrationservice.api.infrastructure.entity.PaymentEntity;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */
@RestController
@RequestMapping("/api/balance")
@RequiredArgsConstructor
public class BalanceAndPaymentOrchestrationController {
	private final BalanceChargeFacade balanceFacade;
	private final BalanceCrudFacade balanceCrudFacade;
	private final PaymentFacade paymentFacade;

	// Mock 데이터 저장소
	private static final Map<Long, BigDecimal> userBalances = new HashMap<>();
	private static final Map<Long, PaymentEntity> payments = new HashMap<>();
	private static Long paymentIdCounter = 1L;

	// 잔액 충전
	@PostMapping("/charge")
	public BalanceResponse chargeBalance(@RequestBody BalanceChargeRequest request) {
		userBalances.put(request.getUserId(), userBalances.getOrDefault(request.getUserId(), BigDecimal.ZERO).add(request.getAmount()));
		return new BalanceResponse(request.getUserId(), userBalances.get(request.getUserId()));
	}

	// 잔액 조회
	@GetMapping("/{userId}")
	public BalanceResponse getBalance(@PathVariable Long userId) {
		return new BalanceResponse(userId, userBalances.getOrDefault(userId, BigDecimal.ZERO));
	}

	// 결제 처리
	@PostMapping("/payment")
	public PaymentResponse processPayment(@RequestBody PaymentRequest request) {
		BigDecimal userBalance = userBalances.getOrDefault(request.getUserId(), BigDecimal.ZERO);

		if (userBalance.compareTo(request.getAmount()) >= 0) {
			// 결제 처리
			userBalances.put(request.getUserId(), userBalance.subtract(request.getAmount()));
			PaymentEntity paymentEntity = new PaymentEntity(paymentIdCounter++, request.getUserId(), request.getReservationId(), request.getAmount(), PaymentStatus.CONFIRMED);
			payments.put(paymentEntity.getPaymentId(), paymentEntity);

			return createMockSuccessPaymentResponse();
		} else {
			// 잔액 부족
			return createMockFailPaymentResponse();
		}
	}
}