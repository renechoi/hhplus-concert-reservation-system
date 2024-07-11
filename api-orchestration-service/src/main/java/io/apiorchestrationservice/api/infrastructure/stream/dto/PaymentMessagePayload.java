package io.apiorchestrationservice.api.infrastructure.stream.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author : Rene Choi
 * @since : 2024/07/11
 */
@Data
@AllArgsConstructor(staticName = "of")
public class PaymentMessagePayload {
	private  String paymentTransactionId;
	private  String userId;
	private String paymentType;



}
