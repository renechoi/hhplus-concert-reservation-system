package io.paymentservice.common.exception.definitions;

import io.paymentservice.common.model.GlobalResponseCode;
import lombok.Getter;

/**
 * @author : Rene Choi
 * @since : 2024/07/10
 */

@Getter
public class PaymentCancelUnAvailableException extends ServerException {

	private final Object additionalInfo;

	public PaymentCancelUnAvailableException(GlobalResponseCode code, Object additionalInfo) {
		super(code);
		this.additionalInfo = additionalInfo;
	}

	public PaymentCancelUnAvailableException(GlobalResponseCode code, Object additionalInfo, Throwable cause) {
		super(code, cause);
		this.additionalInfo = additionalInfo;
	}
}
