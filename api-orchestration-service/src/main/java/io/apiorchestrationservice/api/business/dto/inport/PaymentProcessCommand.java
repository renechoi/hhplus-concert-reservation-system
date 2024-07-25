package io.apiorchestrationservice.api.business.dto.inport;

import java.math.BigDecimal;

import io.apiorchestrationservice.api.business.domainmodel.PaymentMethod;
import io.apiorchestrationservice.api.business.domainmodel.PaymentTarget;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentProcessCommand {
	private Long userId;
	private String targetId; // 예약, 상품, 서비스 등 다양한 분야를 포괄할 수 있는 식별자 -> current context only covers reservationId
	private BigDecimal amount;
	private PaymentMethod paymentMethod;

	private PaymentTarget paymentTarget;

	public PaymentProcessCommand withIdentifiedPaymentTarget() {
		this.targetId = paymentTarget.getTitle() + "-" + targetId;
		return this;
	}
}
