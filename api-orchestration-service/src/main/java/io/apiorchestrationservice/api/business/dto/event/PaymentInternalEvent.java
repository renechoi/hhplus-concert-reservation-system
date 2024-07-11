package io.apiorchestrationservice.api.business.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentInternalEvent {
	private String paymentTransactionId;
	private String userId;
	private String paymentType; // COMPLETED or CANCELLED
}