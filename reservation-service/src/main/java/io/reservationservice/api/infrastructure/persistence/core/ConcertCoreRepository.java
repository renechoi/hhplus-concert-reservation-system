package io.reservationservice.api.infrastructure.persistence.core;

import org.springframework.stereotype.Repository;

import io.reservationservice.api.business.domainentity.Concert;
import io.reservationservice.api.business.persistence.ConcertRepository;
import io.reservationservice.api.infrastructure.persistence.orm.ConcertJpaRepository;
import io.reservationservice.common.exception.definitions.ConcertNotFoundException;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
@Repository
@RequiredArgsConstructor
public class ConcertCoreRepository implements ConcertRepository {
	private final ConcertJpaRepository concertJpaRepository;

	@Override
	public Concert save(Concert entity) {
		return concertJpaRepository.save(entity);
	}

	@Override
	public Concert findById(Long concertId) {
		return concertJpaRepository.findById(concertId).orElseThrow(ConcertNotFoundException::new);
	}
}
