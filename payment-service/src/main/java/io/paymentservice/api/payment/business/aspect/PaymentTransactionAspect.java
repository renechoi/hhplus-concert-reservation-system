package io.paymentservice.api.payment.business.aspect;

import static io.paymentservice.api.balance.business.dto.inport.UserBalanceChargeCommand.*;
import static io.paymentservice.api.balance.business.dto.inport.UserBalanceUseCommand.*;
import static io.paymentservice.api.payment.business.dto.outport.PaymentInfo.*;
import static io.paymentservice.common.model.GlobalResponseCode.*;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import io.paymentservice.api.balance.business.operators.balancecharger.UserBalanceCharger;
import io.paymentservice.api.balance.business.operators.balanceusemanager.UserBalanceUseManager;
import io.paymentservice.api.payment.business.dto.inport.PaymentCommand;
import io.paymentservice.api.payment.business.dto.outport.PaymentInfo;
import io.paymentservice.common.exception.definitions.PaymentProcessUnAvailableException;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/10
 */

@Aspect
@Component
@RequiredArgsConstructor
public class PaymentTransactionAspect {

	private final UserBalanceUseManager userBalanceUseManager;
	private final UserBalanceCharger userBalanceCharger;

	@Around("execution(* io.paymentservice.api.payment.business.service.PaymentService.processPayment(..)) && args(paymentCommand)")
	public PaymentInfo manageTransaction(ProceedingJoinPoint joinPoint, PaymentCommand paymentCommand) throws Throwable {
		try {
			userBalanceUseManager.use(paymentCommand(paymentCommand.getUserId(), paymentCommand.getAmount()));
		} catch (Exception e) {
			throw new PaymentProcessUnAvailableException(USER_BALANCE_USE_UNAVAILABLE, createFailed(paymentCommand), e);
		}

		try {
			return (PaymentInfo) joinPoint.proceed();
		} catch (Exception e) {
			userBalanceCharger.charge(rollbackCommand(paymentCommand.getUserId(), paymentCommand.getAmount()));
			throw new PaymentProcessUnAvailableException(PAYMENT_PROCESSING_FAILED, createFailed(paymentCommand), e);
		}
	}
}