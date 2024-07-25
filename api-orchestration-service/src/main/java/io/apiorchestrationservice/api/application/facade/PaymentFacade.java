package io.apiorchestrationservice.api.application.facade;

import static io.apiorchestrationservice.api.application.dto.response.PaymentResponse.*;
import static io.apiorchestrationservice.api.business.dto.inport.ReservationConfirmCommand.*;
import static java.util.concurrent.TimeUnit.*;

import org.springframework.context.ApplicationEventPublisher;

import io.apiorchestrationservice.api.application.dto.request.PaymentProcessRequest;
import io.apiorchestrationservice.api.application.dto.response.PaymentResponse;
import io.apiorchestrationservice.api.business.service.PaymentService;
import io.apiorchestrationservice.api.business.service.ReservationService;
import io.apiorchestrationservice.common.annotation.DistributedLock;
import io.apiorchestrationservice.common.annotation.Facade;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */
@Facade
@RequiredArgsConstructor
public class PaymentFacade {
	private final PaymentService paymentService;
	private final ReservationService reservationService;
	private final ApplicationEventPublisher applicationEventPublisher;

	/**
	 * 사용자 예약에 대한 결제 처리
	 * 실패시 cancel 요청으로 payment 및 balance에 대한 롤백
	 *
	 * 결제에 대한 중복 요청 validation은 payment service에서 책임
	 */
	@DistributedLock(prefix = "api-orchestration-payment", keys={"#request.userId", "#request.targetId", "#request.amount", "#request.paymentTarget"} , timeUnit = MILLISECONDS, waitTime = 0, leaseTime = 500)
	public PaymentResponse processPayment(PaymentProcessRequest request) {

		PaymentResponse paymentResponse = PaymentResponse.from(paymentService.processPayment(request.toCommand()));
		try{
			reservationService.confirmReservation(createConfirmCommand(request));
		} catch (Exception e){
			paymentService.cancelPayment(paymentResponse.transactionId());
			return createRolledBackResponse(paymentResponse);
		}

		applicationEventPublisher.publishEvent(paymentResponse.toPaymentInternalEventAsComplete());
		return paymentResponse;
	}
}
