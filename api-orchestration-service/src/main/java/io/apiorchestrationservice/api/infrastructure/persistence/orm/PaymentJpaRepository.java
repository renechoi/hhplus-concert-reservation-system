package io.apiorchestrationservice.api.infrastructure.persistence.orm;

import org.springframework.data.jpa.repository.JpaRepository;

import io.apiorchestrationservice.api.infrastructure.entity.PaymentEntity;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */
public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, Long> {
}
