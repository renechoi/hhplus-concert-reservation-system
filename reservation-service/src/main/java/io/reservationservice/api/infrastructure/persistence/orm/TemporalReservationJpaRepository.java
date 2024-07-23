package io.reservationservice.api.infrastructure.persistence.orm;

import org.springframework.data.jpa.repository.JpaRepository;

import io.reservationservice.api.business.domainentity.TemporalReservation;
import io.reservationservice.api.infrastructure.persistence.querydsl.TemporalReservationQueryDslCustomRepository;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
public interface TemporalReservationJpaRepository extends JpaRepository<TemporalReservation, Long>, TemporalReservationQueryDslCustomRepository {
}
