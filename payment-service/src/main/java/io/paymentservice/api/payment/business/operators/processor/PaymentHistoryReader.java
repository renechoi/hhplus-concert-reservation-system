package io.paymentservice.api.payment.business.operators.processor;

import java.util.List;

import org.springframework.stereotype.Component;

import io.paymentservice.api.payment.business.dto.outport.PaymentInfo;
import io.paymentservice.api.payment.business.dto.outport.PaymentInfos;
import io.paymentservice.api.payment.business.persistence.PaymentTransactionRepository;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/10
 */

@Component
@RequiredArgsConstructor
public class PaymentHistoryReader {

	private final PaymentTransactionRepository paymentTransactionRepository;

	public PaymentInfos retrievePaymentHistory(Long userId) {
		List<PaymentInfo> paymentInfos = paymentTransactionRepository.findListByUserId(userId)
			.stream()
			.map(PaymentInfo::from)
			.toList();
		return new PaymentInfos(paymentInfos);
	}
}