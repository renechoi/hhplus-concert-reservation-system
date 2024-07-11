package io.reservationservice.testhelpers.contextholder;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import io.reservationservice.api.application.dto.request.ConcertCreateRequest;
import io.reservationservice.api.application.dto.request.ConcertOptionCreateRequest;
import io.reservationservice.api.application.dto.response.ConcertCreateResponse;
import io.reservationservice.api.application.dto.response.ConcertOptionCreateResponse;

/**
 * @author : Rene Choi
 * @since : 2024/07/08
 */
public class ConcertContextHolder implements TestDtoContextHolder {
	private static final ConcurrentHashMap<Long, ConcertCreateResponse> concertResponseMap = new ConcurrentHashMap<>();
	private static final ConcurrentHashMap<Long, ConcertOptionCreateResponse> concertOptionResponseMap = new ConcurrentHashMap<>();
	private static final ConcurrentHashMap<Long, ConcertCreateRequest> concertRequestMap = new ConcurrentHashMap<>();
	private static final ConcurrentHashMap<Long, ConcertOptionCreateRequest> concertOptionRequestMap = new ConcurrentHashMap<>();
	private static final AtomicReference<Long> mostRecentConcertId = new AtomicReference<>();
	private static final AtomicReference<Long> mostRecentConcertOptionId = new AtomicReference<>();

	public static void initFields() {
		concertResponseMap.clear();
		concertOptionResponseMap.clear();
		concertRequestMap.clear();
		concertOptionRequestMap.clear();
		mostRecentConcertId.set(null);
		mostRecentConcertOptionId.set(null);
	}

	public static void putConcertCreateRequest(Long concertId, ConcertCreateRequest request) {
		concertRequestMap.put(concertId, request);
		mostRecentConcertId.set(concertId);
	}

	public static ConcertCreateRequest getConcertCreateRequest(Long concertId) {
		return concertRequestMap.get(concertId);
	}

	public static void putConcertCreateResponse(Long concertId, ConcertCreateResponse response) {
		concertResponseMap.put(concertId, response);
		mostRecentConcertId.set(concertId);
	}

	public static void putConcertCreateResponse(ConcertCreateResponse response) {
		putConcertCreateResponse(response.concertId(), response);
	}

	public static ConcertCreateResponse getConcertResponse(Long concertId) {
		return concertResponseMap.get(concertId);
	}

	public static void putConcertOptionCreateRequest(Long concertOptionId, ConcertOptionCreateRequest request) {
		concertOptionRequestMap.put(concertOptionId, request);
		mostRecentConcertOptionId.set(concertOptionId);
	}

	public static ConcertOptionCreateRequest getConcertOptionCreateRequest(Long concertOptionId) {
		return concertOptionRequestMap.get(concertOptionId);
	}

	public static void putConcertOptionCreateResponse(Long concertOptionId, ConcertOptionCreateResponse response) {
		concertOptionResponseMap.put(concertOptionId, response);
		mostRecentConcertOptionId.set(concertOptionId);
	}

	public static void putConcertOptionCreateResponse(ConcertOptionCreateResponse response) {
		putConcertOptionCreateResponse(response.concertOptionId(), response);
	}

	public static ConcertOptionCreateResponse getConcertOptionCreateResponse(Long concertOptionId) {
		return concertOptionResponseMap.get(concertOptionId);
	}

	public static Long getMostRecentConcertId() {
		return mostRecentConcertId.get();
	}

	public static Long getMostRecentConcertOptionId() {
		return mostRecentConcertOptionId.get();
	}

	public static ConcertCreateResponse getMostRecentConcertCreateResponse() {
		Long recentConcertId = mostRecentConcertId.get();
		return recentConcertId != null ? getConcertResponse(recentConcertId) : null;
	}

	public static ConcertOptionCreateResponse getMostRecentConcertOptionCreateResponse() {
		Long recentConcertOptionId = mostRecentConcertOptionId.get();
		return recentConcertOptionId != null ? getConcertOptionCreateResponse(recentConcertOptionId) : null;
	}
}
