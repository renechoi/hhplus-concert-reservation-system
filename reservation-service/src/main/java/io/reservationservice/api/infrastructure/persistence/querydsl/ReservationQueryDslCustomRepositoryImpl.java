package io.reservationservice.api.infrastructure.persistence.querydsl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;

import io.reservationservice.api.business.domainentity.QReservation;
import io.reservationservice.api.business.domainentity.Reservation;
import io.reservationservice.api.business.dto.inport.ReservationSearchCommand;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/08
 */
@Component
@RequiredArgsConstructor
public class ReservationQueryDslCustomRepositoryImpl implements ReservationQueryDslCustomRepository {
	private final JPAQueryFactory queryFactory;
	private final QueryFilter<ReservationSearchCommand> queryFilter;

	@Override
	public List<Reservation> findMultipleByCondition(ReservationSearchCommand searchCommand) {
		QReservation reservation = QReservation.reservation;

		Predicate searchPredicate = queryFilter.createGlobalSearchQuery(searchCommand);

		return
			queryFactory.selectFrom(reservation)
				.where(searchPredicate)
				.fetch();
	}
}
