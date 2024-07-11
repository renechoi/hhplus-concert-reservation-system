package io.reservationservice.api.infrastructure.persistence.querydsl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;

import io.reservationservice.api.business.domainentity.QTemporaryReservation;
import io.reservationservice.api.business.dto.inport.TemporaryReservationSearchCommand;
import io.reservationservice.util.QueryDslBooleanExpressionBuilder;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
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
			.notNullAnd(TEMPORARY_RESERVATION.isConfirmed::eq, searchCommand.getIsConfirmed());

		Optional.ofNullable(searchCommand.getReserveAt()).ifPresent(reserveAt ->
			builder.notEmptyAnd(this::createDatePredicate, searchCommand.getDateSearchCondition(), reserveAt.toString())
		);

		return builder.build();
	}

	private BooleanExpression createDatePredicate(String valueCondition, String value) {
		LocalDateTime dateTime = LocalDateTime.parse(value);
		if ("after".equals(valueCondition)) {
			return TEMPORARY_RESERVATION.reserveAt.after(dateTime);
		} else if ("before".equals(valueCondition)) {
			return TEMPORARY_RESERVATION.reserveAt.before(dateTime);
		} else if ("on".equals(valueCondition)) {
			return TEMPORARY_RESERVATION.reserveAt.eq(dateTime);
		} else {
			return null;
		}
	}

}
