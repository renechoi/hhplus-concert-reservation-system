package io.reservationservice.testhelpers.contextholder;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import io.reservationservice.api.application.dto.request.ReservationCreateRequest;
import io.reservationservice.api.application.dto.response.ReservationConfirmResponse;
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
	private static final ConcurrentHashMap<Long, ReservationConfirmResponse> reservationConfirmResponseMap = new ConcurrentHashMap<>();
	private static final AtomicReference<Long> mostRecentReservationConfirmId = new AtomicReference<>();

	public static void initFields() {
		reservationResponseMap.clear();
		reservationRequestMap.clear();
		reservationStatusResponseMap.clear();
		mostRecentReservationId.set(null);
		mostRecentUserId.set(null);
		mostRecentConcertOptionId.set(null);
		reservationConfirmResponseMap.clear();
		mostRecentReservationConfirmId.set(null);
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




	// 예약 확정 응답 저장 메서드
	public static void putReservationConfirmResponse(Long reservationId, ReservationConfirmResponse response) {
		reservationConfirmResponseMap.put(reservationId, response);
		mostRecentReservationConfirmId.set(reservationId);
	}

	// 최근 예약 확정 응답 저장 메서드
	public static void putReservationConfirmResponse(ReservationConfirmResponse response) {
		Long reservationId = getMostRecentReservationId();
		putReservationConfirmResponse(reservationId, response);
	}

	public static ReservationConfirmResponse getReservationConfirmResponse(Long reservationId) {
		return reservationConfirmResponseMap.get(reservationId);
	}

	public static ReservationConfirmResponse getMostRecentReservationConfirmResponse() {
		Long recentReservationId = mostRecentReservationConfirmId.get();
		return recentReservationId != null ? getReservationConfirmResponse(recentReservationId) : null;
	}
}