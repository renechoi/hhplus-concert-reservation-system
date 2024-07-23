package io.reservationservice.testhelpers.contextholder;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import io.reservationservice.api.application.dto.request.ReservationCreateRequest;
import io.reservationservice.api.application.dto.response.ReservationStatusResponses;
import io.reservationservice.api.application.dto.response.TemporalReservationCreateResponse;

/**
 * @author : Rene Choi
 * @since : 2024/07/08
 */

public class ReservationContextHolder implements TestDtoContextHolder {
	private static final ConcurrentHashMap<Long, TemporalReservationCreateResponse> reservationResponseMap = new ConcurrentHashMap<>();
	private static final ConcurrentHashMap<Long, ReservationCreateRequest> reservationRequestMap = new ConcurrentHashMap<>();
	private static final ConcurrentHashMap<Long, ReservationStatusResponses> reservationStatusResponseMap = new ConcurrentHashMap<>();
	private static final AtomicReference<Long> mostRecentReservationId = new AtomicReference<>();
	private static final AtomicReference<Long> mostRecentUserId = new AtomicReference<>();
	private static final AtomicReference<Long> mostRecentConcertOptionId = new AtomicReference<>();

	public static void initFields() {
		reservationResponseMap.clear();
		reservationRequestMap.clear();
		reservationStatusResponseMap.clear();
		mostRecentReservationId.set(null);
		mostRecentUserId.set(null);
		mostRecentConcertOptionId.set(null);
	}

	public static void putReservationCreateRequest(Long reservationId, ReservationCreateRequest request) {
		reservationRequestMap.put(reservationId, request);
		mostRecentReservationId.set(reservationId);
	}

	public static ReservationCreateRequest getReservationCreateRequest(Long reservationId) {
		return reservationRequestMap.get(reservationId);
	}

	public static void putReservationCreateResponse(Long reservationId, TemporalReservationCreateResponse response) {
		reservationResponseMap.put(reservationId, response);
		mostRecentReservationId.set(reservationId);
	}

	public static void putReservationCreateResponse(TemporalReservationCreateResponse response) {
		putReservationCreateResponse(response.temporalReservationId(), response);
	}

	public static TemporalReservationCreateResponse getReservationResponse(Long reservationId) {
		return reservationResponseMap.get(reservationId);
	}

	public static Long getMostRecentReservationId() {
		return mostRecentReservationId.get();
	}

	public static TemporalReservationCreateResponse getMostRecentReservationCreateResponse() {
		Long recentReservationId = mostRecentReservationId.get();
		return recentReservationId != null ? getReservationResponse(recentReservationId) : null;
	}

	public static void putReservationStatusResponse(Long userId, Long concertOptionId, ReservationStatusResponses response) {
		reservationStatusResponseMap.put(userId, response);
		mostRecentUserId.set(userId);
		mostRecentConcertOptionId.set(concertOptionId);
	}



	public static ReservationStatusResponses getReservationStatusResponse(Long userId) {
		return reservationStatusResponseMap.get(userId);
	}

	public static ReservationStatusResponses getMostRecentReservationStatusResponse() {
		Long recentUserId = mostRecentUserId.get();
		return recentUserId != null ? getReservationStatusResponse(recentUserId) : null;
	}
}