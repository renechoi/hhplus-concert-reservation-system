package io.queuemanagement.api.interfaces.stream.payload;

import io.queuemanagement.api.application.dto.request.CompletedTokenHandlingRequest;
import io.queuemanagement.common.mapper.ObjectMapperBasedVoMapper;
import lombok.Data;

/**
 * @author : Rene Choi
 * @since : 2024/07/11
 */
@Data
public class PaymentMessagePayload {
	private  String paymentTransactionId;
	private  String userId;

	public CompletedTokenHandlingRequest toCompletedTokenHandlingRequest() {
		return ObjectMapperBasedVoMapper.convert(this, CompletedTokenHandlingRequest.class);
	}
}
