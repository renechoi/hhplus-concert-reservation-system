package io.paymentservice.api.payment.business.operators.processor;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.paymentservice.api.payment.business.dto.inport.PaymentCommand;
import io.paymentservice.api.payment.business.dto.outport.PaymentInfo;
import io.paymentservice.api.payment.business.persistence.PaymentTransactionRepository;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/10
 */

@Component
@RequiredArgsConstructor
public class PaymentProcessor {

	private final PaymentTransactionRepository paymentTransactionRepository;

	@Transactional
	public PaymentInfo process(PaymentCommand paymentCommand) {
		return	PaymentInfo.from(paymentTransactionRepository.save(paymentCommand.toEntity().withCompleted()));
	}
}
