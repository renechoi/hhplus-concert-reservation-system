package io.reservationservice.api.infrastructure.persistence.orm;

import org.springframework.data.jpa.repository.JpaRepository;

import io.reservationservice.api.business.domainentity.Reservation;
import io.reservationservice.api.infrastructure.persistence.querydsl.ReservationQueryDslCustomRepository;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
public interface ReservationJpaRepository extends JpaRepository<Reservation, Long>, ReservationQueryDslCustomRepository {
}
