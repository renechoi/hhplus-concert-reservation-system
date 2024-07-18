package io.reservationservice.api.business.dto.outport;

import java.util.Collections;
import java.util.List;

/**
 * @author : Rene Choi
 * @since : 2024/07/11
 */
public record ReservationStatusInfos(List<ReservationStatusInfo> reservationStatusInfos) {
	public ReservationStatusInfos {
		reservationStatusInfos = reservationStatusInfos != null ? List.copyOf(reservationStatusInfos) : Collections.emptyList();
	}
}
