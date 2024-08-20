package io.reservationservice.api.infrastructure.persistence.orm;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.reservationservice.api.business.domainentity.OutboxEvent;
import io.reservationservice.api.business.domainentity.OutboxStatus;

/**
 * @author : Rene Choi
 * @since : 2024/08/15
 */
public interface OutboxJpaRepository extends JpaRepository<OutboxEvent, Long> {
	List<OutboxEvent> findByStatusAndCreatedAtBefore(OutboxStatus status, LocalDateTime timestamp);
}
