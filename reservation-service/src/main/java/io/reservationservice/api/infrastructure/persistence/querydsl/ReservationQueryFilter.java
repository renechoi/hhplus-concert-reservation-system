package io.reservationservice.api.infrastructure.persistence.querydsl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;

import io.reservationservice.api.business.domainentity.QReservation;
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
			.notNullAnd(reservation.isCanceled::eq, searchCommand.getIsCanceled());

		Optional.ofNullable(searchCommand.getReserveAt()).ifPresent(reserveAt ->
			builder.notEmptyAnd(this::createDatePredicate, searchCommand.getDateSearchCondition(), reserveAt.toString())
		);

		return builder.build();
	}

	private BooleanExpression createDatePredicate(String valueCondition, String value) {
		LocalDateTime dateTime = LocalDateTime.parse(value);
		if ("after".equals(valueCondition)) {
			return reservation.reserveAt.after(dateTime);
		} else if ("before".equals(valueCondition)) {
			return reservation.reserveAt.before(dateTime);
		} else if ("on".equals(valueCondition)) {
			return reservation.reserveAt.eq(dateTime);
		} else {
			return null;
		}
	}

}
