package io.apiorchestrationservice.testhelpers.contextholder;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import io.apiorchestrationservice.api.application.dto.response.AvailableDatesResponses;
import io.apiorchestrationservice.api.application.dto.response.AvailableSeatsResponses;
import io.apiorchestrationservice.api.application.dto.response.ConcertCreateResponse;
import io.apiorchestrationservice.api.application.dto.response.ConcertOptionCreateResponse;
import io.apiorchestrationservice.api.application.dto.response.ReservationCreateResponse;

/**
 * @author : Rene Choi
 * @since : 2024/07/11
 */

public class ReservationAndConcertContextHolder implements TestDtoContextHolder {
	private static final ConcurrentHashMap<Long, ConcertCreateResponse> concertMap = new ConcurrentHashMap<>();
	private static final ConcurrentHashMap<Long, ConcertOptionCreateResponse> concertOptionMap = new ConcurrentHashMap<>();
	private static final ConcurrentHashMap<Long, AvailableDatesResponses> availableDatesMap = new ConcurrentHashMap<>();
	private static final ConcurrentHashMap<Long, AvailableSeatsResponses> availableSeatsMap = new ConcurrentHashMap<>();
	private static final ConcurrentHashMap<Long, ReservationCreateResponse> reservationMap = new ConcurrentHashMap<>();
	private static final AtomicReference<Long> mostRecentConcertId = new AtomicReference<>();
	private static final AtomicReference<Long> mostRecentConcertOptionId = new AtomicReference<>();
	private static final AtomicReference<Long> mostRecentReservationId = new AtomicReference<>();
	private static final AtomicReference<ConcertCreateResponse> mostRecentConcertCreateResponse = new AtomicReference<>();
	private static final AtomicReference<AvailableDatesResponses> mostRecentAvailableDatesResponse = new AtomicReference<>();
	private static final AtomicReference<ConcertOptionCreateResponse> mostRecentConcertOptionCreateResponse = new AtomicReference<>();
	private static final AtomicReference<ReservationCreateResponse> mostRecentReservationCreateResponse = new AtomicReference<>();


	public static void initFields() {
		concertMap.clear();
		concertOptionMap.clear();
		availableDatesMap.clear();
		availableSeatsMap.clear();
		reservationMap.clear();
		mostRecentConcertId.set(null);
		mostRecentConcertOptionId.set(null);
		mostRecentReservationId.set(null);
		mostRecentConcertCreateResponse.set(null);
		mostRecentAvailableDatesResponse.set(null);
		mostRecentConcertOptionCreateResponse.set(null);
		mostRecentReservationCreateResponse.set(null);
	}

	public static void putConcert(Long concertId, ConcertCreateResponse response) {
		concertMap.put(concertId, response);
		mostRecentConcertId.set(concertId);
	}

	public static ConcertCreateResponse getConcert(Long concertId) {
		return concertMap.get(concertId);
	}

	public static void putConcertOption(Long concertOptionId, ConcertOptionCreateResponse response) {
		concertOptionMap.put(concertOptionId, response);
		mostRecentConcertOptionId.set(concertOptionId);
		mostRecentConcertOptionCreateResponse.set(response);
	}

	public static ConcertOptionCreateResponse getConcertOption(Long concertOptionId) {
		return concertOptionMap.get(concertOptionId);
	}

	public static void putAvailableDates(Long concertId, AvailableDatesResponses response) {
		availableDatesMap.put(concertId, response);
		mostRecentConcertId.set(concertId);
		mostRecentAvailableDatesResponse.set(response);
	}

	public static AvailableDatesResponses getAvailableDates(Long concertOptionId) {
		return availableDatesMap.get(concertOptionId);
	}

	public static AvailableDatesResponses getMostRecentAvailableDatesResponse() {
		return mostRecentAvailableDatesResponse.get();
	}

	public static void putAvailableSeats(Long concertOptionId, AvailableSeatsResponses response) {
		availableSeatsMap.put(concertOptionId, response);
		mostRecentConcertOptionId.set(concertOptionId);
	}

	public static AvailableSeatsResponses getAvailableSeats(Long concertOptionId) {
		return availableSeatsMap.get(concertOptionId);
	}

	public static void putReservation(Long reservationId, ReservationCreateResponse response) {
		reservationMap.put(reservationId, response);
		mostRecentReservationId.set(reservationId);
	}

	public static void putReservation(ReservationCreateResponse response) {
		putReservation(response.temporaryReservationId(), response);
		mostRecentReservationCreateResponse.set(response);
	}

	public static ReservationCreateResponse getReservation(Long reservationId) {
		return reservationMap.get(reservationId);
	}

	public static Long getMostRecentConcertId() {
		return mostRecentConcertId.get();
	}

	public static Long getMostRecentConcertOptionId() {
		return mostRecentConcertOptionId.get();
	}

	public static Long getMostRecentReservationId() {
		return mostRecentReservationId.get();
	}

	public static void putConcertCreateResponse(ConcertCreateResponse response) {
		mostRecentConcertCreateResponse.set(response);
		mostRecentConcertId.set(response.concertId());
	}

	public static ConcertCreateResponse getMostRecentConcertCreateResponse() {
		return mostRecentConcertCreateResponse.get();
	}

	public static void putConcertOptionCreateResponse(ConcertOptionCreateResponse response) {
		mostRecentConcertOptionCreateResponse.set(response);
		mostRecentConcertOptionId.set(response.concertOptionId());
	}

	public static ConcertOptionCreateResponse getMostRecentConcertOptionCreateResponse() {
		return mostRecentConcertOptionCreateResponse.get();
	}



	public static AvailableSeatsResponses getMostRecentAvailableSeatsResponse() {
		Long recentConcertOptionId = mostRecentConcertOptionId.get();
		return recentConcertOptionId != null ? getAvailableSeats(recentConcertOptionId) : null;
	}

	public static ReservationCreateResponse getMostRecentReservationCreateResponse() {
		return mostRecentReservationCreateResponse.get();
	}
}