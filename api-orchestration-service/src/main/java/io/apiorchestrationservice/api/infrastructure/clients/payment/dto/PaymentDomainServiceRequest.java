package io.apiorchestrationservice.api.infrastructure.clients.payment.dto;

import java.math.BigDecimal;

import io.apiorchestrationservice.api.business.domainmodel.PaymentMethod;
import io.apiorchestrationservice.api.business.dto.inport.PaymentProcessCommand;
import io.apiorchestrationservice.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/07/10
 */
public record PaymentDomainServiceRequest(
    Long userId,
    BigDecimal amount,
	PaymentMethod paymentMethod
) {
	public static PaymentDomainServiceRequest from(PaymentProcessCommand command) {
		return ObjectMapperBasedVoMapper.convert(command, PaymentDomainServiceRequest.class);
	}
}
