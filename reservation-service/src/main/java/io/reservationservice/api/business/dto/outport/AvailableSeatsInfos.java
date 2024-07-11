package io.reservationservice.api.business.dto.outport;

import java.util.Collections;
import java.util.List;

/**
 * @author : Rene Choi
 * @since : 2024/07/08
 */
public record AvailableSeatsInfos(List<AvailableSeatsInfo> availableDateInfos) {
	public AvailableSeatsInfos {
		availableDateInfos = availableDateInfos != null ? List.copyOf(availableDateInfos) : Collections.emptyList();
	}

	public static AvailableSeatsInfos from(List<AvailableSeatsInfo> availableDateInfos) {
		return new AvailableSeatsInfos(availableDateInfos);
	}
}
