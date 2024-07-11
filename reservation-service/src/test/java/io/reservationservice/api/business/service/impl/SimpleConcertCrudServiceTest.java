package io.reservationservice.api.business.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.reservationservice.api.business.domainentity.Concert;
import io.reservationservice.api.business.domainentity.ConcertOption;
import io.reservationservice.api.business.dto.inport.ConcertCreateCommand;
import io.reservationservice.api.business.dto.inport.ConcertOptionCreateCommand;
import io.reservationservice.api.business.dto.outport.ConcertCreateInfo;
import io.reservationservice.api.business.persistence.ConcertOptionRepository;
import io.reservationservice.api.business.persistence.ConcertRepository;
import io.reservationservice.api.business.persistence.SeatRepository;

/**
 * @author : Rene Choi
 * @since : 2024/07/12
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("SimpleConcertCrudService 테스트")
class SimpleConcertCrudServiceTest {

	@Mock
	private ConcertRepository concertRepository;

	@Mock
	private ConcertOptionRepository concertOptionRepository;

	@Mock
	private SeatRepository seatRepository;

	@InjectMocks
	private SimpleConcertCrudService simpleConcertCrudService;

	private ConcertCreateCommand concertCreateCommand;
	private ConcertOptionCreateCommand concertOptionCreateCommand;
	private Concert concert;
	private ConcertOption concertOption;

	@BeforeEach
	void setUp() {
		concertCreateCommand = ConcertCreateCommand.builder()
			.title("Sample Concert")
			.build();

		concertOptionCreateCommand = ConcertOptionCreateCommand.builder()
			.concertDate(LocalDateTime.now().plusDays(10))
			.concertDuration(Duration.ofHours(2))
			.title("VIP")
			.description("VIP Pass")
			.price(BigDecimal.valueOf(100))
			.maxSeats(10)
			.build();

		concert = Concert.builder()
			.concertId(1L)
			.title("Sample Concert")
			.build();

		concertOption = ConcertOption.builder()
			.concertOptionId(1L)
			.concert(concert)
			.concertDate(concertOptionCreateCommand.getConcertDate())
			.concertDuration(concertOptionCreateCommand.getConcertDuration())
			.title(concertOptionCreateCommand.getTitle())
			.description(concertOptionCreateCommand.getDescription())
			.price(concertOptionCreateCommand.getPrice())
			.build();
	}

	@Test
	@DisplayName("콘서트 생성 테스트")
	void testCreateConcert() {
		// given
		when(concertRepository.save(any(Concert.class))).thenReturn(concert);

		// when
		ConcertCreateInfo concertCreateInfo = simpleConcertCrudService.createConcert(concertCreateCommand);

		// then
		assertNotNull(concertCreateInfo);
		assertEquals(concert.getConcertId(), concertCreateInfo.concertId());
		assertEquals(concert.getTitle(), concertCreateInfo.title());
	}


}
