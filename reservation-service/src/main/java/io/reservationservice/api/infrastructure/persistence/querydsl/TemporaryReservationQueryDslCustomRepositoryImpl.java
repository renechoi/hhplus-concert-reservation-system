package io.reservationservice.api.infrastructure.persistence.querydsl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;

import io.reservationservice.api.business.domainentity.QTemporaryReservation;
import io.reservationservice.api.business.domainentity.TemporaryReservation;
import io.reservationservice.api.business.dto.inport.TemporaryReservationSearchCommand;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/08
 */
@Component
@RequiredArgsConstructor
public class TemporaryReservationQueryDslCustomRepositoryImpl implements TemporaryReservationQueryDslCustomRepository {
	private final JPAQueryFactory queryFactory;
	private final QueryFilter<TemporaryReservationSearchCommand> queryFilter;

	@Override
	public List<TemporaryReservation> findMultipleByCondition(TemporaryReservationSearchCommand searchCommand) {
		QTemporaryReservation temporaryReservation = QTemporaryReservation.temporaryReservation;

		Predicate searchPredicate = queryFilter.createGlobalSearchQuery(searchCommand);

		return
			queryFactory.selectFrom(temporaryReservation)
				.where(searchPredicate)
				.fetch();
	}
}
