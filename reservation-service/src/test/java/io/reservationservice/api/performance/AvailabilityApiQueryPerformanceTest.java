package io.reservationservice.api.performance;

import java.util.Random;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import io.reservationservice.api.business.dto.inport.ConcertOptionSearchCommand;
import io.reservationservice.api.business.dto.inport.SeatSearchCommand;
import io.reservationservice.api.business.persistence.ConcertOptionRepository;
import io.reservationservice.api.business.persistence.SeatRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : Rene Choi
 * @since : 2024/08/05
 */
@SpringBootTest
@Slf4j
@ActiveProfiles("stage-test")
public class AvailabilityApiQueryPerformanceTest {

	@Autowired
	private ConcertOptionRepository concertOptionRepository;

	@Autowired
	private SeatRepository seatRepository;

	private static final int TEST_SET_SIZE = 100;
	private static final Random random = new Random();
	private static final Long[] concertIds = new Long[TEST_SET_SIZE*10];
	private static final Long[] concertOptionIds = new Long[TEST_SET_SIZE*10];

	@BeforeAll
	public static void setup() {
		for (int i = 0; i < TEST_SET_SIZE; i++) {
			concertIds[i] = (long) (random.nextInt(100) + 1);  //  1 ~ 100
			concertOptionIds[i] = (long) (random.nextInt(100) + 1);  //  1 ~ 100
		}
	}

	@BeforeEach
	public void warmUp() {
		for (int i = 0; i < 100; i++) {  // warm-up queries
			Long concertId = concertIds[random.nextInt(TEST_SET_SIZE, TEST_SET_SIZE*2)];
			concertOptionRepository.findMultipleByCondition(ConcertOptionSearchCommand.onUpcomingDates(concertId));

			Long concertOptionId = concertOptionIds[random.nextInt(TEST_SET_SIZE, TEST_SET_SIZE*2)];
			seatRepository.findMultipleByCondition(SeatSearchCommand.onConcertOption(concertOptionId));
		}
	}

	@Test
	@DisplayName("예약 가능 날짜 조회 쿼리 성능 테스트")
	public void testGetAvailableDatesPerformance() {
		int iterations = 100;
		long totalTime = 0;

		for (int i = 0; i < iterations; i++) {
			Long concertId = concertIds[i % TEST_SET_SIZE];  // random concertId
			long startTime = System.nanoTime();
			concertOptionRepository.findMultipleByCondition(ConcertOptionSearchCommand.onUpcomingDates(concertId));
			long endTime = System.nanoTime();
			long duration = (endTime - startTime) / 1_000_000;  // nanoseconds -> milliseconds
			totalTime += duration;
		}

		double averageTime = totalTime / (double) iterations;
		log.info("Average execution time for getAvailableDates: {} ms", averageTime);
	}

	@Test
	@DisplayName("예약 가능 좌석 조회 쿼리 성능 테스트")
	public void testGetAvailableSeatsPerformance() {
		int iterations = 100;
		long totalTime = 0;

		for (int i = 0; i < iterations; i++) {
			Long concertOptionId = concertOptionIds[i % TEST_SET_SIZE];  // random concertOptionId
			long startTime = System.nanoTime();
			seatRepository.findMultipleByCondition(SeatSearchCommand.onConcertOption(concertOptionId));
			long endTime = System.nanoTime();
			long duration = (endTime - startTime) / 1_000_000;  // nanoseconds -> milliseconds
			totalTime += duration;
		}

		double averageTime = totalTime / (double) iterations;
		log.info("Average execution time for getAvailableSeats: {} ms", averageTime);
	}
}
