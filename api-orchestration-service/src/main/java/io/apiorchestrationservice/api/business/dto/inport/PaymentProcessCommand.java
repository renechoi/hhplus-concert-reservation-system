package io.apiorchestrationservice.api.business.dto.inport;

import java.math.BigDecimal;

import io.apiorchestrationservice.api.business.domainmodel.PaymentMethod;
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
	private Long reservationId;  // temporal
	private BigDecimal amount;
	private PaymentMethod paymentMethod;
}
