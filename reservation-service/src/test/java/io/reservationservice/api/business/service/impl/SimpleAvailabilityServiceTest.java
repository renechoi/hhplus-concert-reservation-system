package io.reservationservice.api.business.service.impl;

import static io.reservationservice.api.business.dto.inport.DateSearchCommand.DateSearchCondition.*;
import static io.reservationservice.api.business.dto.inport.SeatSearchCommand.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.reservationservice.api.business.domainentity.ConcertOption;
import io.reservationservice.api.business.domainentity.Seat;
import io.reservationservice.api.business.dto.outport.AvailableDateInfos;
import io.reservationservice.api.business.dto.outport.AvailableSeatsInfos;
import io.reservationservice.api.business.persistence.ConcertOptionRepository;
import io.reservationservice.api.business.persistence.SeatRepository;

/**
 * @author : Rene Choi
 * @since : 2024/07/12
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("SimpleAvailabilityService 테스트")
class SimpleAvailabilityServiceTest {

	@Mock
	private ConcertOptionRepository concertOptionRepository;

	@Mock
	private SeatRepository seatRepository;

	@InjectMocks
	private SimpleAvailabilityService simpleAvailabilityService;

	@Test
	@DisplayName("사용 가능한 날짜를 가져오는 테스트")
	void testGetAvailableDates() {
		// given
		Long concertId = 1L;
		List<ConcertOption> concertOptions = Arrays.asList(
			ConcertOption.builder().concertOptionId(1L).concertDate(LocalDateTime.now().plusDays(1)).title("Concert 1").build(),
			ConcertOption.builder().concertOptionId(2L).concertDate(LocalDateTime.now().plusDays(2)).title("Concert 2").build()
		);

		when(concertOptionRepository.findMultipleBy(argThat(command ->
			command.getConcertId().equals(concertId) && AFTER.equals(command.getDateSearchCondition())
		))).thenReturn(concertOptions);

		// when
		AvailableDateInfos availableDates = simpleAvailabilityService.getAvailableDates(concertId);

		// then
		assertEquals(2, availableDates.availableDateInfos().size());
		verify(concertOptionRepository, times(1)).findMultipleBy(argThat(command ->
			command.getConcertId().equals(concertId) && AFTER.equals(command.getDateSearchCondition())
		));
	}

	@Test
	@DisplayName("사용 가능한 좌석을 가져오는 테스트")
	void testGetAvailableSeats() {
		Long concertOptionId = 1L;
		Long requestAt = System.currentTimeMillis();
		List<Seat> seats = Arrays.asList(
			Seat.builder().seatId(1L).concertOption(ConcertOption.builder().concertOptionId(concertOptionId).concertDate(LocalDateTime.now().plusDays(1)).build()).seatNumber(1L).occupied(false).build(),
			Seat.builder().seatId(2L).concertOption(ConcertOption.builder().concertOptionId(concertOptionId).concertDate(LocalDateTime.now().plusDays(2)).build()).seatNumber(2L).occupied(false).build()
		);

		when(seatRepository.findMultipleBy(concertOptionId(concertOptionId)))
			.thenReturn(seats);

		AvailableSeatsInfos availableSeats = simpleAvailabilityService.getAvailableSeats(concertOptionId, requestAt);

		assertEquals(2, availableSeats.availableDateInfos().size());
	}
}
