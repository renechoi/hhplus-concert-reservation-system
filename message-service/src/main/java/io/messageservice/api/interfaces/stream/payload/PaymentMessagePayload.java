package io.messageservice.api.interfaces.stream.payload;

import io.messageservice.common.mapper.ObjectMapperBasedVoMapper;
import lombok.Data;

/**
 * @author : Rene Choi
 * @since : 2024/07/11
 */
@Data
public class PaymentMessagePayload {
	private  String paymentTransactionId;
	private  String userId;


}
