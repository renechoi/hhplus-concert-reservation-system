package io.reservationservice.testhelpers.fixture;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

import io.reservationservice.api.business.domainentity.Concert;
import io.reservationservice.api.business.domainentity.ConcertOption;
import io.reservationservice.api.business.domainentity.Seat;

/**
 * @author : Rene Choi
 * @since : 2024/08/05
 */

public class FixtureFactory {

	public static Concert createConcert() {
		return Concert.builder()
			.title("Concert " + System.currentTimeMillis())
			.createdAt(LocalDateTime.now())
			.requestAt(LocalDateTime.now())
			.build();
	}

	public static ConcertOption createConcertOption() {
		Concert concert = createConcert();
		return ConcertOption.builder()
			.concert(concert)
			.concertDate(LocalDateTime.now().plusDays(30))
			.concertDuration(Duration.ofHours(2))
			.title("Concert Option " + System.currentTimeMillis())
			.description("Description")
			.price(BigDecimal.valueOf(100.0))
			.createdAt(LocalDateTime.now())
			.requestAt(LocalDateTime.now())
			.build();
	}

	public static Seat createSeat(ConcertOption concertOption) {
		return Seat.builder()
			.concertOption(concertOption)
			.seatNumber(System.currentTimeMillis())
			.occupied(false)
			.createdAt(LocalDateTime.now())
			.build();
	}
}
