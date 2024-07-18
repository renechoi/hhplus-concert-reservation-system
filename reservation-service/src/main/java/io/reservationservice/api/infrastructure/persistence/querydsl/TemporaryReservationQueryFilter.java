package io.reservationservice.api.infrastructure.persistence.querydsl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTimePath;

import io.reservationservice.api.business.domainentity.QTemporaryReservation;
import io.reservationservice.api.business.dto.inport.TemporaryReservationSearchCommand;
import io.reservationservice.util.QueryDslBooleanExpressionBuilder;
import lombok.RequiredArgsConstructor;

/**
 * Author: Rene Choi
 * Since: 2024/07/07
 */
@Component
@RequiredArgsConstructor
public class TemporaryReservationQueryFilter implements QueryFilter<TemporaryReservationSearchCommand> {

	private static final QTemporaryReservation TEMPORARY_RESERVATION = QTemporaryReservation.temporaryReservation;

	@Override
	public Predicate createGlobalSearchQuery(TemporaryReservationSearchCommand searchCommand) {
		QueryDslBooleanExpressionBuilder builder = new QueryDslBooleanExpressionBuilder(TEMPORARY_RESERVATION.isNotNull())
			.notNullAnd(TEMPORARY_RESERVATION.userId::eq, searchCommand.getUserId())
			.notNullAnd(TEMPORARY_RESERVATION.concertOption.concertOptionId::eq, searchCommand.getConcertOptionId())
			.notNullAnd(TEMPORARY_RESERVATION.seat.seatId::eq, searchCommand.getSeatId())
			.notNullAnd(TEMPORARY_RESERVATION.isConfirmed::eq, searchCommand.getIsConfirmed())

			.and(createDatePredicate(searchCommand.getDateSearchCondition(), searchCommand.getReserveAt(), TEMPORARY_RESERVATION.reserveAt))
			.and(createDatePredicate(searchCommand.getDateSearchCondition(), searchCommand.getExpireAt(), TEMPORARY_RESERVATION.expireAt))
			.and(createDatePredicate(searchCommand.getDateSearchCondition(), searchCommand.getCreatedAt(), TEMPORARY_RESERVATION.createdAt))
			.and(createDatePredicate(searchCommand.getDateSearchCondition(), searchCommand.getRequestAt(), TEMPORARY_RESERVATION.requestAt));

		return builder.build();
	}

	private BooleanExpression createDatePredicate(String condition, LocalDateTime date, DateTimePath<LocalDateTime> dateTimePath) {
		if (condition == null || date == null) {
			return null;
		}
		switch (condition) {
			case "before":
				return dateTimePath.before(date);
			case "after":
				return dateTimePath.after(date);
			case "on":
				return dateTimePath.eq(date);
			default:
				return null;
		}
	}
}
