package io.reservationservice.api.business.dto.outport;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * @author : Rene Choi
 * @since : 2024/07/08
 */
public record AvailableDateInfos(List<AvailableDateInfo> availableDateInfos) {

	@JsonCreator
	public AvailableDateInfos {
		availableDateInfos = availableDateInfos != null ? List.copyOf(availableDateInfos) : Collections.emptyList();
	}

	public static AvailableDateInfos from(List<AvailableDateInfo> availableDateInfos) {
		return new AvailableDateInfos(availableDateInfos);
	}
}
