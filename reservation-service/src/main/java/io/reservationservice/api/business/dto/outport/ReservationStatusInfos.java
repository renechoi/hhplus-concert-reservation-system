package io.reservationservice.api.business.dto.outport;

import static io.reservationservice.common.model.GlobalResponseCode.*;
import static org.springframework.util.CollectionUtils.*;

import java.util.Collections;
import java.util.List;

import io.reservationservice.common.exception.definitions.ReservationUnAvailableException;

/**
 * @author : Rene Choi
 * @since : 2024/07/11
 */
public record ReservationStatusInfos(List<ReservationStatusInfo> reservationStatusInfos) {
	public ReservationStatusInfos {
		reservationStatusInfos = reservationStatusInfos != null ? List.copyOf(reservationStatusInfos) : Collections.emptyList();
	}

	public ReservationStatusInfos withValidated() {
		if (isEmpty(this.reservationStatusInfos)) {
			throw new ReservationUnAvailableException(RESERVATION_RETRIEVAL_NO_CONTENT);
		}
		return this;
	}
}
