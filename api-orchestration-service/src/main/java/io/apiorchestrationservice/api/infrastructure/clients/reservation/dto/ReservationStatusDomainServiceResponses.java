package io.apiorchestrationservice.api.infrastructure.clients.reservation.dto;

import java.util.Collections;
import java.util.List;

import io.apiorchestrationservice.api.business.dto.outport.ReservationStatusInfos;

/**
 * @author : Rene Choi
 * @since : 2024/07/11
 */
public record ReservationStatusDomainServiceResponses(List<ReservationStatusDomainServiceResponse> reservationStatusResponses) {
	public ReservationStatusDomainServiceResponses {
		reservationStatusResponses = reservationStatusResponses != null ? List.copyOf(reservationStatusResponses) : Collections.emptyList();
	}

	public ReservationStatusInfos toReservationStatusInfos() {
		return new ReservationStatusInfos(
			reservationStatusResponses.stream()
				.map(ReservationStatusDomainServiceResponse::toReservationStatusInfo)
				.toList()
		);
	}
}
