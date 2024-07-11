package io.reservationservice.testhelpers.contextholder;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import io.reservationservice.api.application.dto.response.AvailableDatesResponses;
import io.reservationservice.api.application.dto.response.AvailableSeatsResponses;

/**
 * @author : Rene Choi
 * @since : 2024/07/08
 */

public class AvailabilityContextHolder {
	private static final ConcurrentHashMap<Long, AvailableDatesResponses> availableDatesMap = new ConcurrentHashMap<>();
	private static final ConcurrentHashMap<Long, AvailableSeatsResponses> availableSeatsMap = new ConcurrentHashMap<>();
	private static final AtomicReference<Long> mostRecentConcertId = new AtomicReference<>();
	private static final AtomicReference<Long> mostRecentConcertOptionId = new AtomicReference<>();

	public static void initFields() {
		availableDatesMap.clear();
		availableSeatsMap.clear();
		mostRecentConcertId.set(null);
		mostRecentConcertOptionId.set(null);
	}

	public static void putAvailableDates(Long concertId, AvailableDatesResponses response) {
		availableDatesMap.put(concertId, response);
		mostRecentConcertId.set(concertId);
	}

	public static AvailableDatesResponses getAvailableDates(Long concertId) {
		return availableDatesMap.get(concertId);
	}

	public static void putAvailableSeats(Long concertOptionId, AvailableSeatsResponses response) {
		availableSeatsMap.put(concertOptionId, response);
		mostRecentConcertOptionId.set(concertOptionId);
	}

	public static AvailableSeatsResponses getAvailableSeats(Long concertOptionId) {
		return availableSeatsMap.get(concertOptionId);
	}

	public static Long getMostRecentConcertId() {
		return mostRecentConcertId.get();
	}

	public static Long getMostRecentConcertOptionId() {
		return mostRecentConcertOptionId.get();
	}

	public static AvailableDatesResponses getMostRecentAvailableDatesResponse() {
		Long recentConcertId = mostRecentConcertId.get();
		return recentConcertId != null ? getAvailableDates(recentConcertId) : null;
	}

	public static AvailableSeatsResponses getMostRecentAvailableSeatsResponse() {
		Long recentConcertOptionId = mostRecentConcertOptionId.get();
		return recentConcertOptionId != null ? getAvailableSeats(recentConcertOptionId) : null;
	}
}