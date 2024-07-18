package io.reservationservice.api.application.dto.response;

import java.util.Collections;
import java.util.List;

import io.reservationservice.api.business.dto.outport.ReservationStatusInfos;

/**
 * @author : Rene Choi
 * @since : 2024/07/11
 */
public record ReservationStatusResponses(List<ReservationStatusResponse> reservationStatusResponses) {
	public ReservationStatusResponses {
		reservationStatusResponses = reservationStatusResponses != null ? List.copyOf(reservationStatusResponses) : Collections.emptyList();
	}

	public static ReservationStatusResponses from(ReservationStatusInfos reservationStatus) {
		return new ReservationStatusResponses(reservationStatus.reservationStatusInfos().stream()
			.map(ReservationStatusResponse::from)
			.toList());
	}
}
