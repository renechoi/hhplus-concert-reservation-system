package io.reservationservice.api.infrastructure.persistence.orm;

import org.springframework.data.jpa.repository.JpaRepository;

import io.reservationservice.api.business.domainentity.ConcertOption;
import io.reservationservice.api.infrastructure.persistence.querydsl.ConcertOptionQueryDslCustomRepository;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
public interface ConcertOptionJpaRepository extends JpaRepository<ConcertOption, Long>, ConcertOptionQueryDslCustomRepository {
}
