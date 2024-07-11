package io.reservationservice.api.infrastructure.persistence.querydsl;

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

	@Override
	public Optional<Seat> findSingleByCondition(SeatSearchCommand searchCommand) {
		QSeat seat = QSeat.seat;

		Predicate searchPredicate = queryFilter.createGlobalSearchQuery(searchCommand);

		return Optional.ofNullable(
			queryFactory.selectFrom(seat)
				.where(searchPredicate)
				.fetchOne()
		);
	}

	@Override
	public List<Seat> findMultipleByCondition(SeatSearchCommand searchCommand) {
		QSeat seat = QSeat.seat;

		Predicate searchPredicate = queryFilter.createGlobalSearchQuery(searchCommand);

		return
			queryFactory.selectFrom(seat)
				.where(searchPredicate)
				.fetch();
	}
}
