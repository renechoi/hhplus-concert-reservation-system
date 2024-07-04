package io.apiorchestrationservice.api.application.facade;

import io.apiorchestrationservice.api.business.service.PaymentService;
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
}
