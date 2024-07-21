package io.reservationservice.api.infrastructure.persistence.querydsl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTimePath;

import io.reservationservice.api.business.domainentity.QConcertOption;
import io.reservationservice.api.business.dto.inport.ConcertOptionSearchCommand;
import io.reservationservice.api.business.dto.inport.DateSearchCommand;
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
		QueryDslBooleanExpressionBuilder builder = new QueryDslBooleanExpressionBuilder(concertOption.isNotNull())
			.notNullAnd(concertOption.concertOptionId::eq, searchCommand.getConcertOptionId())
			.notNullAnd(concertOption.concert.concertId::eq, searchCommand.getConcertId())
			.notEmptyAnd(concertOption.title::containsIgnoreCase, searchCommand.getTitle())
			.notEmptyAnd(concertOption.description::containsIgnoreCase, searchCommand.getDescription())
			.notNullAnd(concertOption.price::eq, searchCommand.getPrice())
			.and(createDatePredicate(searchCommand.getDateSearchCondition(), searchCommand.getConcertDate(), concertOption.concertDate))
			.and(createDatePredicate(searchCommand.getDateSearchCondition(), searchCommand.getCreatedAt(), concertOption.createdAt))
			.and(createDatePredicate(searchCommand.getDateSearchCondition(), searchCommand.getRequestAt(), concertOption.requestAt));

		return builder.build();
	}

	private BooleanExpression createDatePredicate(DateSearchCommand.DateSearchCondition condition, LocalDateTime date, DateTimePath<LocalDateTime> dateTimePath) {
		if (condition == null || date == null) {
			return null;
		}
		switch (condition) {
			case BEFORE:
				return dateTimePath.before(date);
			case AFTER:
				return dateTimePath.after(date);
			case ON:
				return dateTimePath.eq(date);
			default:
				return null;
		}
	}

}
