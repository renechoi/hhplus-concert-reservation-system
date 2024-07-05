package io.apiorchestrationservice.api.infrastructure.persistence.core;

import org.springframework.stereotype.Repository;

import io.apiorchestrationservice.api.business.persistence.ReservationStoreRepository;
import io.apiorchestrationservice.api.infrastructure.persistence.orm.ReservationJpaRepository;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */
@Repository
@RequiredArgsConstructor
public class SimpleReservationStoreRepository implements ReservationStoreRepository {
	private final ReservationJpaRepository reservationJpaRepository;

}
