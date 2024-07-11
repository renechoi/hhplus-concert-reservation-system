package io.reservationservice.api.infrastructure.persistence.orm;

import org.springframework.data.jpa.repository.JpaRepository;

import io.reservationservice.api.business.domainentity.Concert;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
public interface ConcertJpaRepository extends JpaRepository<Concert, Long> {
}
