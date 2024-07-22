package io.paymentservice.api.payment.business.dto.outport;

import static io.paymentservice.api.payment.business.entity.PaymentStatus.*;

import java.math.BigDecimal;

import io.paymentservice.api.payment.business.entity.PaymentStatus;
import io.paymentservice.api.payment.business.entity.PaymentTransaction;
import io.paymentservice.api.payment.business.dto.inport.PaymentCommand;
import io.paymentservice.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/07/10
 */
public record PaymentInfo(
	Long transactionId,
	Long userId,
	BigDecimal amount,
	PaymentStatus paymentStatus
) {

	public static PaymentInfo from(PaymentTransaction paymentTransaction) {
		return ObjectMapperBasedVoMapper.convert(paymentTransaction, PaymentInfo.class);
	}

	public static PaymentInfo createFailed(PaymentCommand paymentCommand) {
		return new PaymentInfo(null, paymentCommand.getUserId(), paymentCommand.getAmount(), FAILED);
	}
}
