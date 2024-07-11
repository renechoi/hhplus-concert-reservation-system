package io.apiorchestrationservice.api.application.dto.request;

import java.math.BigDecimal;

import io.apiorchestrationservice.api.business.domainmodel.PaymentMethod;
import io.apiorchestrationservice.api.business.dto.inport.PaymentProcessCommand;
import io.apiorchestrationservice.common.mapper.ObjectMapperBasedVoMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentProcessRequest {
	private Long userId;
	private Long reservationId; // temporal
	private BigDecimal amount;
	private PaymentMethod paymentMethod;

	public PaymentProcessCommand toCommand() {
		return ObjectMapperBasedVoMapper.convert(this, PaymentProcessCommand.class);
	}
}