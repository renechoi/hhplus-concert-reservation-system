package io.apiorchestrationservice.api.infrastructure.persistence.core;

import org.springframework.stereotype.Repository;

import io.apiorchestrationservice.api.business.persistence.PaymentRepository;
import io.apiorchestrationservice.api.infrastructure.persistence.orm.PaymentJpaRepository;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */
@Repository
@RequiredArgsConstructor
public class PaymentCoreRepository implements PaymentRepository {
	private final PaymentJpaRepository paymentJpaRepository;
}
