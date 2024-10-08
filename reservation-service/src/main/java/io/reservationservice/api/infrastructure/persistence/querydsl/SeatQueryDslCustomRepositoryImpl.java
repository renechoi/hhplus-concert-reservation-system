package io.reservationservice.api.infrastructure.persistence.querydsl;

import static jakarta.persistence.LockModeType.*;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;

import io.reservationservice.api.business.domainentity.QSeat;
import io.reservationservice.api.business.domainentity.Seat;
import io.reservationservice.api.business.dto.inport.SeatSearchCommand;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
@Component
@RequiredArgsConstructor
public class SeatQueryDslCustomRepositoryImpl implements SeatQueryDslCustomRepository {
	private final JPAQueryFactory queryFactory;
	private final QueryFilter<SeatSearchCommand> queryFilter;
	private final QuerySorter<Seat> querySorter;

	private final QSeat seat = QSeat.seat;
	@Override
	public Optional<Seat> findSingleByCondition(SeatSearchCommand searchCommand) {
		Predicate searchPredicate = queryFilter.createGlobalSearchQuery(searchCommand);

		return Optional.ofNullable(
			queryFactory.selectFrom(seat)
				.where(searchPredicate)
				.fetchOne()
		);
	}

	@Override
	public List<Seat> findMultipleByCondition(SeatSearchCommand searchCommand) {
		Predicate searchPredicate = queryFilter.createGlobalSearchQuery(searchCommand);

		return
			queryFactory.selectFrom(seat)
				.where(searchPredicate)
				.fetch();
	}

	@Override
	public Optional<Seat> findSingleByConditionWithLock(SeatSearchCommand searchCommand) {
		Predicate searchPredicate = queryFilter.createGlobalSearchQuery(searchCommand);

		return Optional.ofNullable(
			queryFactory.selectFrom(seat)
				.where(searchPredicate)
				.setLockMode(PESSIMISTIC_WRITE)
				.fetchOne()
		);
	}

}
