package io.reservationservice.api.application.dto.response;

import java.util.Collections;
import java.util.List;

/**
 * @author : Rene Choi
 * @since : 2024/07/11
 */
public record ReservationStatusResponses(List<ReservationStatusResponse> reservationStatusResponses) {
	public ReservationStatusResponses {
		reservationStatusResponses = reservationStatusResponses != null ? List.copyOf(reservationStatusResponses) : Collections.emptyList();
	}
}
