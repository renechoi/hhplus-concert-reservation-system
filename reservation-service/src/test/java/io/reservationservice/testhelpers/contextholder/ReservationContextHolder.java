package io.reservationservice.testhelpers.contextholder;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import io.reservationservice.api.application.dto.request.ReservationCreateRequest;
import io.reservationservice.api.application.dto.response.TemporaryReservationCreateResponse;

/**
 * @author : Rene Choi
 * @since : 2024/07/08
 */

public class ReservationContextHolder implements TestDtoContextHolder {
	private static final ConcurrentHashMap<Long, TemporaryReservationCreateResponse> reservationResponseMap = new ConcurrentHashMap<>();
	private static final ConcurrentHashMap<Long, ReservationCreateRequest> reservationRequestMap = new ConcurrentHashMap<>();
	private static final AtomicReference<Long> mostRecentReservationId = new AtomicReference<>();

	public static void initFields() {
		reservationResponseMap.clear();
		reservationRequestMap.clear();
		mostRecentReservationId.set(null);
	}

	public static void putReservationCreateRequest(Long reservationId, ReservationCreateRequest request) {
		reservationRequestMap.put(reservationId, request);
		mostRecentReservationId.set(reservationId);
	}

	public static ReservationCreateRequest getReservationCreateRequest(Long reservationId) {
		return reservationRequestMap.get(reservationId);
	}

	public static void putReservationCreateResponse(Long reservationId, TemporaryReservationCreateResponse response) {
		reservationResponseMap.put(reservationId, response);
		mostRecentReservationId.set(reservationId);
	}

	public static void putReservationCreateResponse(TemporaryReservationCreateResponse response) {
		putReservationCreateResponse(response.temporaryReservationId(), response);
	}

	public static TemporaryReservationCreateResponse getReservationResponse(Long reservationId) {
		return reservationResponseMap.get(reservationId);
	}

	public static Long getMostRecentReservationId() {
		return mostRecentReservationId.get();
	}

	public static TemporaryReservationCreateResponse getMostRecentReservationCreateResponse() {
		Long recentReservationId = mostRecentReservationId.get();
		return recentReservationId != null ? getReservationResponse(recentReservationId) : null;
	}
}