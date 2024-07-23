package io.reservationservice.api.infrastructure.persistence.querydsl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;

import io.reservationservice.api.business.domainentity.QTemporalReservation;
import io.reservationservice.api.business.domainentity.TemporalReservation;
import io.reservationservice.api.business.dto.inport.TemporalReservationSearchCommand;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/08
 */
@Component
@RequiredArgsConstructor
public class TemporalReservationQueryDslCustomRepositoryImpl implements TemporalReservationQueryDslCustomRepository {
	private final JPAQueryFactory queryFactory;
	private final QueryFilter<TemporalReservationSearchCommand> queryFilter;

	@Override
	public List<TemporalReservation> findMultipleByCondition(TemporalReservationSearchCommand searchCommand) {
		QTemporalReservation temporalReservation = QTemporalReservation.temporalReservation;

		Predicate searchPredicate = queryFilter.createGlobalSearchQuery(searchCommand);

		return
			queryFactory.selectFrom(temporalReservation)
				.where(searchPredicate)
				.fetch();
	}
}
