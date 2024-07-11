package io.reservationservice.api.infrastructure.persistence.querydsl;

import org.springframework.stereotype.Component;

import com.querydsl.core.types.Predicate;

import io.reservationservice.api.business.domainentity.QSeat;
import io.reservationservice.api.business.dto.inport.SeatSearchCommand;
import io.reservationservice.util.QueryDslBooleanExpressionBuilder;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
@Component
@RequiredArgsConstructor
public class SeatQueryFilter implements QueryFilter<SeatSearchCommand> {

	private static final QSeat seat = QSeat.seat;

	@Override
	public Predicate createGlobalSearchQuery(SeatSearchCommand searchCommand) {
		return new QueryDslBooleanExpressionBuilder(seat.isNotNull())
			.notNullAnd(seat.concertOption::eq, searchCommand.getConcertOption())
			.notNullAnd(seat.concertOption.concertOptionId::eq, searchCommand.getConcertOptionId())
			.notNullAnd(seat.seatNumber::eq, searchCommand.getSeatNumber())
			.build();

	}

}
