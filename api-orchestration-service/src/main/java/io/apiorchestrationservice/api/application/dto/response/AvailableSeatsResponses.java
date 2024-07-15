package io.apiorchestrationservice.api.application.dto.response;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import io.apiorchestrationservice.api.infrastructure.clients.reservation.dto.AvailableSeatsInfos;

/**
 * @author : Rene Choi
 * @since : 2024/07/08
 */
public record AvailableSeatsResponses(List<AvailableSeatsResponse> availableSeatsResponses) {
	public AvailableSeatsResponses {
		availableSeatsResponses = availableSeatsResponses != null ? List.copyOf(availableSeatsResponses) : Collections.emptyList();
	}

	public static AvailableSeatsResponses from(AvailableSeatsInfos availableSeatsInfos) {
		List<AvailableSeatsResponse> responses = availableSeatsInfos.availableSeatInfos().stream()
			.map(AvailableSeatsResponse::from)
			.collect(Collectors.toList());

		return new AvailableSeatsResponses(responses);
	}
}
