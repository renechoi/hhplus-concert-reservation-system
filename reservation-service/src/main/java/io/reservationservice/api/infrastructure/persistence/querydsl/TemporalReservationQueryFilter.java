package io.reservationservice.api.infrastructure.persistence.querydsl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTimePath;

import io.reservationservice.api.business.domainentity.QTemporalReservation;
import io.reservationservice.api.business.dto.inport.DateSearchCommand;
import io.reservationservice.api.business.dto.inport.TemporalReservationSearchCommand;
import io.reservationservice.util.QueryDslBooleanExpressionBuilder;
import lombok.RequiredArgsConstructor;

/**
 * Author: Rene Choi
 * Since: 2024/07/07
 */
@Component
@RequiredArgsConstructor
public class TemporalReservationQueryFilter implements QueryFilter<TemporalReservationSearchCommand> {

	private static final QTemporalReservation TEMPORAL_RESERVATION = QTemporalReservation.temporalReservation;


	@Override
	public Predicate createGlobalSearchQuery(TemporalReservationSearchCommand searchCommand) {
		QueryDslBooleanExpressionBuilder builder = new QueryDslBooleanExpressionBuilder(TEMPORAL_RESERVATION.isNotNull())
			.notNullAnd(TEMPORAL_RESERVATION.userId::eq, searchCommand.getUserId())
			.notNullAnd(TEMPORAL_RESERVATION.concertOption.concertOptionId::eq, searchCommand.getConcertOptionId())
			.notNullAnd(TEMPORAL_RESERVATION.seat.seatId::eq, searchCommand.getSeatId())
			.notNullAnd(TEMPORAL_RESERVATION.isConfirmed::eq, searchCommand.getIsConfirmed())

			.and(createDatePredicate(searchCommand.getDateSearchCondition(), searchCommand.getReserveAt(), TEMPORAL_RESERVATION.reserveAt))
			.and(createDatePredicate(searchCommand.getDateSearchCondition(), searchCommand.getExpireAt(), TEMPORAL_RESERVATION.expireAt))
			.and(createDatePredicate(searchCommand.getDateSearchCondition(), searchCommand.getCreatedAt(), TEMPORAL_RESERVATION.createdAt))
			.and(createDatePredicate(searchCommand.getDateSearchCondition(), searchCommand.getRequestAt(), TEMPORAL_RESERVATION.requestAt));

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
