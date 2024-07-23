package io.paymentservice.api.payment.business.dto.inport;

import java.math.BigDecimal;

import io.paymentservice.api.payment.business.entity.PaymentMethod;
import io.paymentservice.api.payment.business.entity.PaymentTransaction;
import io.paymentservice.common.mapper.ObjectMapperBasedVoMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentCommand {

	private Long userId;
	private String targetId; // 예약, 상품, 서비스 등 다양한 분야를 포괄할 수 있는 식별자
	private BigDecimal amount;
	private PaymentMethod paymentMethod;

	public PaymentTransaction toEntity() {
		return ObjectMapperBasedVoMapper.convert(this, PaymentTransaction.class);
	}
}
