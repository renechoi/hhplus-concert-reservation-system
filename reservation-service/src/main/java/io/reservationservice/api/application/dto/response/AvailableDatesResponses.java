package io.reservationservice.api.application.dto.response;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import io.reservationservice.api.business.dto.outport.AvailableDateInfos;

/**
 * @author : Rene Choi
 * @since : 2024/07/08
 */
public record AvailableDatesResponses(List<AvailableDateResponse> availableDateResponses) {
	public AvailableDatesResponses {
		availableDateResponses = availableDateResponses != null ? List.copyOf(availableDateResponses) : Collections.emptyList();
	}

	public static AvailableDatesResponses from(AvailableDateInfos availableDateInfos) {
		List<AvailableDateResponse> responses = availableDateInfos.availableDateInfos().stream()
			.map(AvailableDateResponse::from)
			.collect(Collectors.toList());

		return new AvailableDatesResponses(responses);
	}

}
