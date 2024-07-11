package io.reservationservice.api.infrastructure.persistence.orm;

import org.springframework.data.jpa.repository.JpaRepository;

import io.reservationservice.api.business.domainentity.TemporaryReservation;
import io.reservationservice.api.infrastructure.persistence.querydsl.TemporaryReservationQueryDslCustomRepository;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
public interface TemporalReservationJpaRepository extends JpaRepository<TemporaryReservation, Long>, TemporaryReservationQueryDslCustomRepository {
}
