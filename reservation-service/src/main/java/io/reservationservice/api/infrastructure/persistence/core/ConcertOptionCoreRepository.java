package io.reservationservice.api.infrastructure.persistence.core;

import java.util.List;

import org.springframework.stereotype.Repository;

import io.reservationservice.api.business.domainentity.ConcertOption;
import io.reservationservice.api.business.dto.inport.ConcertOptionSearchCommand;
import io.reservationservice.api.business.persistence.ConcertOptionRepository;
import io.reservationservice.api.infrastructure.persistence.orm.ConcertOptionJpaRepository;
import io.reservationservice.common.exception.definitions.ConcertOptionNotFoundException;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
@Repository
@RequiredArgsConstructor
public class ConcertOptionCoreRepository implements ConcertOptionRepository {

	private final ConcertOptionJpaRepository concertOptionJpaRepository;

	@Override
	public ConcertOption save(ConcertOption concertOption) {
		return concertOptionJpaRepository.save(concertOption);
	}

	@Override
	public ConcertOption findById(Long concertOptionId) {
		return concertOptionJpaRepository.findById(concertOptionId).orElseThrow(ConcertOptionNotFoundException::new);
	}

	@Override
	public ConcertOption findByIdWithSLock(Long concertOptionId) {
		return concertOptionJpaRepository.findByIdWithSLock(concertOptionId).orElseThrow(ConcertOptionNotFoundException::new);
	}

	@Override
	public List<ConcertOption> findMultipleByCondition(ConcertOptionSearchCommand searchCommand) {
		return concertOptionJpaRepository.findMultipleByCondition(searchCommand);
	}
}
