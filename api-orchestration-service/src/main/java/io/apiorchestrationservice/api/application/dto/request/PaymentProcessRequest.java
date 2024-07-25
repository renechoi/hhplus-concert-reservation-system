package io.apiorchestrationservice.api.application.dto.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import io.apiorchestrationservice.api.business.domainmodel.PaymentMethod;
import io.apiorchestrationservice.api.business.domainmodel.PaymentTarget;
import io.apiorchestrationservice.api.business.dto.inport.PaymentProcessCommand;
import io.apiorchestrationservice.api.infrastructure.clients.reservation.dto.AbstractCommonRequestInfo;
import io.apiorchestrationservice.common.mapper.ObjectMapperBasedVoMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentProcessRequest extends AbstractCommonRequestInfo {
	private Long userId;
	private String targetId; // 예약, 상품, 서비스 등 다양한 분야를 포괄할 수 있는 식별자 -> current context only covers reservationId
	private BigDecimal amount;
	private PaymentMethod paymentMethod;

	private PaymentTarget paymentTarget;

	public PaymentProcessCommand toCommand() {
		return ObjectMapperBasedVoMapper.convert(this, PaymentProcessCommand.class);
	}
}