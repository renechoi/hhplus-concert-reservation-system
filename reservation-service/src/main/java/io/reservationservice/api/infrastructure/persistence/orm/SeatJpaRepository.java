package io.reservationservice.api.infrastructure.persistence.orm;

import org.springframework.data.jpa.repository.JpaRepository;

import io.reservationservice.api.business.domainentity.Seat;
import io.reservationservice.api.infrastructure.persistence.querydsl.SeatQueryDslCustomRepository;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
public interface SeatJpaRepository extends JpaRepository<Seat, Long>, SeatQueryDslCustomRepository {
}
