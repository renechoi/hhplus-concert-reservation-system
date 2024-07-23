package io.paymentservice.api.payment.interfaces.dto.request;

import java.math.BigDecimal;

import io.paymentservice.api.payment.business.entity.PaymentMethod;
import io.paymentservice.api.payment.business.dto.inport.PaymentCommand;
import io.paymentservice.common.mapper.ObjectMapperBasedVoMapper;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/10
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {

	@NotNull
	private Long userId;

	@NotEmpty
	private String targetId; // 예약, 상품, 서비스 등 다양한 분야를 포괄할 수 있는 식별자

	@NotNull
	private BigDecimal amount;

	@NotNull
	private PaymentMethod paymentMethod;


	public PaymentCommand toCommand(){
		return ObjectMapperBasedVoMapper.convert(this, PaymentCommand.class);
	}
}
