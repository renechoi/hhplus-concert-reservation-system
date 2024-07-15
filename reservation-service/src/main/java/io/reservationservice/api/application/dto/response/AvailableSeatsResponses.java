package io.reservationservice.api.application.dto.response;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import io.reservationservice.api.business.dto.outport.AvailableSeatsInfos;

/**
 * @author : Rene Choi
 * @since : 2024/07/08
 */
public record AvailableSeatsResponses(List<AvailableSeatsResponse> availableSeatsResponses) {
	public AvailableSeatsResponses {
		availableSeatsResponses = availableSeatsResponses != null ? List.copyOf(availableSeatsResponses) : Collections.emptyList();
	}

	public static AvailableSeatsResponses from(AvailableSeatsInfos availableSeatsInfos) {
		List<AvailableSeatsResponse> responses = availableSeatsInfos.availableDateInfos().stream()
			.map(AvailableSeatsResponse::from)
			.collect(Collectors.toList());

		return new AvailableSeatsResponses(responses);
	}
}
