package io.reservationservice.api.infrastructure.persistence.querydsl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;

import io.reservationservice.api.business.domainentity.QConcertOption;
import io.reservationservice.api.business.dto.inport.ConcertOptionSearchCommand;
import io.reservationservice.util.QueryDslBooleanExpressionBuilder;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
@Component
@RequiredArgsConstructor
public class ConcertOptionQueryFilter implements QueryFilter<ConcertOptionSearchCommand> {

	private static final QConcertOption concertOption = QConcertOption.concertOption;

	@Override
	public Predicate createGlobalSearchQuery(ConcertOptionSearchCommand searchCommand) {
		return new QueryDslBooleanExpressionBuilder(concertOption.isNotNull())
			.notNullAnd(concertOption.concertOptionId::eq, searchCommand.getConcertOptionId())
			.notNullAnd(concertOption.concert.concertId::eq, searchCommand.getConcertId())
			.notEmptyAnd(concertOption.title::containsIgnoreCase, searchCommand.getTitle())
			.notEmptyAnd(concertOption.description::containsIgnoreCase, searchCommand.getDescription())
			.notNullAnd(concertOption.price::eq, searchCommand.getPrice())
			.notEmptyAnd(this::createConcertDatePredicate, searchCommand.getDateSearchCondition(), searchCommand.getConcertDate().toString())
			.build();
	}

	private BooleanExpression createConcertDatePredicate(String valueCondition, String value) {
		if ("after".equals(valueCondition)) {
			return concertOption.concertDate.after(LocalDateTime.parse(value));
		} else if ("before".equals(valueCondition)) {
			return concertOption.concertDate.before(LocalDateTime.parse(value));
		} else if ("on".equals(valueCondition)) {
			return concertOption.concertDate.eq(LocalDateTime.parse(value));
		} else {
			return null;
		}
	}

}
