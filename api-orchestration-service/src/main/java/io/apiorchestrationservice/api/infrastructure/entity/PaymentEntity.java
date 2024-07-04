package io.apiorchestrationservice.api.infrastructure.entity;

import java.math.BigDecimal;

import io.apiorchestrationservice.api.business.domainmodel.PaymentStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long paymentId;

	private Long userId;
	private Long reservationId;
	private BigDecimal amount;
	private PaymentStatus status;


}
