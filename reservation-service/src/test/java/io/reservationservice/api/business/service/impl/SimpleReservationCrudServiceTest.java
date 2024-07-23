package io.reservationservice.api.business.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.reservationservice.api.business.domainentity.ConcertOption;
import io.reservationservice.api.business.domainentity.Reservation;
import io.reservationservice.api.business.domainentity.Seat;
import io.reservationservice.api.business.domainentity.TemporalReservation;
import io.reservationservice.api.business.dto.inport.ReservationCreateCommand;
import io.reservationservice.api.business.dto.outport.ReservationConfirmInfo;
import io.reservationservice.api.business.dto.outport.TemporalReservationCreateInfo;
import io.reservationservice.api.business.persistence.ConcertOptionRepository;
import io.reservationservice.api.business.persistence.ReservationRepository;
import io.reservationservice.api.business.persistence.SeatRepository;
import io.reservationservice.api.business.persistence.TemporalReservationRepository;
import io.reservationservice.common.exception.definitions.ReservationUnAvailableException;
import io.reservationservice.common.model.GlobalResponseCode;

/**
 * @author : Rene Choi
 * @since : 2024/07/12
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("SimpleReservationCrudService의 테스트 클래스")
public class SimpleReservationCrudServiceTest {

	@InjectMocks
	private SimpleReservationCrudService simpleReservationCrudService;

	@Mock
	private ReservationRepository reservationRepository;

	@Mock
	private TemporalReservationRepository temporalReservationRepository;

	@Mock
	private ConcertOptionRepository concertOptionRepository;

	@Mock
	private SeatRepository seatRepository;

	@Test
	@DisplayName("임시 예약 생성 성공 테스트")
	public void testCreateTemporalReservationSuccess() {
		// Given
		ReservationCreateCommand command = ReservationCreateCommand.builder()
			.concertOptionId(1L)
			.seatNumber(10L)
			.userId(1L)
			.build();

		ConcertOption concertOption = mock(ConcertOption.class);
		Seat seat = mock(Seat.class);

		when(concertOptionRepository.findById(anyLong())).thenReturn(concertOption);
		when(seatRepository.findSingleByCondition(any())).thenReturn(seat);
		when(seatRepository.save(any())).thenReturn(seat);
		TemporalReservation temporalReservation = mock(TemporalReservation.class);
		when(temporalReservationRepository.save(any())).thenReturn(temporalReservation);

		// When
		TemporalReservationCreateInfo result = simpleReservationCrudService.createTemporalReservation(command);

		// Then
		assertNotNull(result);
		verify(concertOptionRepository).findById(command.getConcertOptionId());
		verify(seatRepository).findSingleByCondition(any());
	}


	@Test
	@DisplayName("좌석이 이미 예약된 경우 임시 예약 생성 실패 테스트")
	public void testCreateTemporaryReservationSeatOccupied() {
		// Given
		ReservationCreateCommand command = ReservationCreateCommand.builder()
			.concertOptionId(1L)
			.seatNumber(10L)
			.userId(1L)
			.build();

		ConcertOption concertOption = mock(ConcertOption.class);
		Seat seat = mock(Seat.class);

		when(concertOptionRepository.findById(anyLong())).thenReturn(concertOption);
		when(seatRepository.findSingleByCondition(any())).thenReturn(seat);
		doThrow(new ReservationUnAvailableException(GlobalResponseCode.SEAT_ALREADY_RESERVED)).when(seat).doReserve();

		// When
		ReservationUnAvailableException exception = assertThrows(ReservationUnAvailableException.class, () -> simpleReservationCrudService.createTemporalReservation(command));

		// Then
		assertEquals(GlobalResponseCode.SEAT_ALREADY_RESERVED, exception.getCode());
		verify(concertOptionRepository).findById(command.getConcertOptionId());
		verify(seatRepository).findSingleByCondition(any());
	}

	@Test
	@DisplayName("임시 예약 확정 성공 테스트")
	public void testConfirmReservationSuccess() {
		// Given
		Long temporaryReservationId = 1L;

		TemporalReservation temporalReservation = mock(TemporalReservation.class);
		Reservation reservation = mock(Reservation.class);

		when(temporalReservationRepository.findById(anyLong())).thenReturn(temporalReservation);
		when(temporalReservation.toConfirmedReservation()).thenReturn(reservation);
		when(reservationRepository.save(any())).thenReturn(reservation);

		// When
		ReservationConfirmInfo result = simpleReservationCrudService.confirmReservation(temporaryReservationId);

		// Then
		assertNotNull(result);
		verify(temporalReservationRepository).findById(temporaryReservationId);
		verify(temporalReservation).confirm();
		verify(reservationRepository).save(reservation);
	}

	@Test
	@DisplayName("임시 예약 취소 성공 테스트")
	public void testCancelTemporaryReservationSuccess() {
		// Given
		Long temporalReservationId = 1L;

		TemporalReservation temporalReservation = mock(TemporalReservation.class);

		when(temporalReservationRepository.findById(anyLong())).thenReturn(temporalReservation);

		// When
		simpleReservationCrudService.cancelTemporalReservation(temporalReservationId);

		// Then
		verify(temporalReservationRepository).findById(temporalReservationId);
		verify(temporalReservation).doCancelSeat();
		verify(temporalReservation).cancelConfirm();
	}

}
