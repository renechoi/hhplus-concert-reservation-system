package io.reservationservice.cucumber.steps;

import static io.reservationservice.testhelpers.apiexecutor.ReservationApiExecutor.*;
import static io.reservationservice.testhelpers.contextholder.ReservationContextHolder.*;
import static io.reservationservice.testhelpers.parser.ReservationResponseParser.*;
import static io.reservationservice.util.FieldMapper.*;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import io.cucumber.java8.En;
import io.reservationservice.api.application.dto.request.ReservationCreateRequest;

/**
 * @author : Rene Choi
 * @since : 2024/07/23
 */

public class ConcurrentReservationApiStepDef implements En {

	public ConcurrentReservationApiStepDef() {
		When("각기 다른 random 유저가 동일한 좌석에 대해 {int}개의 예약 생성 요청을 동시에 보낸다", this::createTwoReservationsConcurrently);

	}

	private void createTwoReservationsConcurrently(int numberOfRequests) {
		List<Map<String, String>> userInfos = generateRandomUserInfos(numberOfRequests);

		ExecutorService executorService = Executors.newFixedThreadPool(numberOfRequests);
		CountDownLatch latch = new CountDownLatch(1);
		CountDownLatch doneLatch = new CountDownLatch(numberOfRequests);

		for (Map<String, String> userInfo : userInfos) {
			executorService.submit(() -> {
				try {
					latch.await();
					createReservationWithSuccessResponse(userInfo);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				} finally {
					doneLatch.countDown();
				}
			});
		}

		latch.countDown(); // 모든 스레드가 준비되면 동시에 시작
		awaitTermination(executorService, doneLatch);
	}

	private void createReservationWithSuccessResponse(Map<String, String> reservationData) {
		putReservationCreateResponse(parseReservationCreateResponse(createReservationWithCreated(updateFields(new ReservationCreateRequest(), reservationData))));
	}

	private List<Map<String, String>> generateRandomUserInfos(int count) {
		Random random = new Random();
		String seatNumber = String.valueOf(random.nextInt(100));
		return IntStream.range(0, count)
			.mapToObj(i -> Map.of(
				"userId", String.valueOf(random.nextInt(100000000)),
				"concertOptionId", "1",
				"seatNumber", seatNumber
			))
			.collect(Collectors.toList());
	}

	private void awaitTermination(ExecutorService executorService, CountDownLatch doneLatch) {
		try {
			doneLatch.await(20, TimeUnit.SECONDS);
			executorService.shutdown();
			if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
				executorService.shutdownNow();
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			executorService.shutdownNow();
		}
	}
}