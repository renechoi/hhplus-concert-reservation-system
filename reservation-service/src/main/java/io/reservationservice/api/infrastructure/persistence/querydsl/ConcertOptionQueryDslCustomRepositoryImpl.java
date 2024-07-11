package io.reservationservice.api.infrastructure.persistence.querydsl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;

import io.reservationservice.api.business.domainentity.ConcertOption;
import io.reservationservice.api.business.domainentity.QConcertOption;
import io.reservationservice.api.business.dto.inport.ConcertOptionSearchCommand;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/08
 */
@Component
@RequiredArgsConstructor
public class ConcertOptionQueryDslCustomRepositoryImpl implements ConcertOptionQueryDslCustomRepository {
	private final JPAQueryFactory queryFactory;
	private final QueryFilter<ConcertOptionSearchCommand> queryFilter;

	@Override
	public List<ConcertOption> findMultipleByCondition(ConcertOptionSearchCommand searchCommand) {
		QConcertOption qConcertOption = QConcertOption.concertOption;

		Predicate searchPredicate = queryFilter.createGlobalSearchQuery(searchCommand);

		return
			queryFactory.selectFrom(qConcertOption)
				.where(searchPredicate)
				.fetch();

	}
}
