package io.reservationservice.api.infrastructure.persistence.querydsl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTimePath;

import io.reservationservice.api.business.domainentity.QReservation;
import io.reservationservice.api.business.dto.inport.DateSearchCommand;
import io.reservationservice.api.business.dto.inport.DateSearchCommand.DateSearchCondition;
import io.reservationservice.api.business.dto.inport.ReservationSearchCommand;
import io.reservationservice.util.QueryDslBooleanExpressionBuilder;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
@Component
@RequiredArgsConstructor
public class ReservationQueryFilter implements QueryFilter<ReservationSearchCommand> {

	private static final QReservation reservation = QReservation.reservation;



	@Override
	public Predicate createGlobalSearchQuery(ReservationSearchCommand searchCommand) {
		QueryDslBooleanExpressionBuilder builder = new QueryDslBooleanExpressionBuilder(reservation.isNotNull())
			.notNullAnd(reservation.userId::eq, searchCommand.getUserId())
			.notNullAnd(reservation.concertOption.concertOptionId::eq, searchCommand.getConcertOptionId())
			.notNullAnd(reservation.seat.seatId::eq, searchCommand.getSeatId())
			.notNullAnd(reservation.isCanceled::eq, searchCommand.getIsCanceled())

			.and(createDatePredicate(searchCommand.getDateSearchCondition(), searchCommand.getReserveAt(), reservation.reserveAt))
			.and(createDatePredicate(searchCommand.getDateSearchCondition(), searchCommand.getTemporaryReservationReserveAt(), reservation.temporaryReservationReserveAt))
			.and(createDatePredicate(searchCommand.getDateSearchCondition(), searchCommand.getCreatedAt(), reservation.createdAt))
			.and(createDatePredicate(searchCommand.getDateSearchCondition(), searchCommand.getRequestAt(), reservation.requestAt));

		return builder.build();
	}

	private BooleanExpression createDatePredicate(DateSearchCondition condition, LocalDateTime date, DateTimePath<LocalDateTime> dateTimePath) {
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
