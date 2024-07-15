package io.apiorchestrationservice.api.application.dto.response;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import io.apiorchestrationservice.api.business.dto.outport.AvailableDatesInfos;

/**
 * @author : Rene Choi
 * @since : 2024/07/11
 */
public record AvailableDatesResponses(List<AvailableDatesResponse> availableDateResponses) {
	public AvailableDatesResponses {
		availableDateResponses = availableDateResponses != null ? List.copyOf(availableDateResponses) : Collections.emptyList();
	}

	public static AvailableDatesResponses from(AvailableDatesInfos availableDateInfos) {
		List<AvailableDatesResponse> responses = availableDateInfos.availableDateInfos().stream()
			.map(AvailableDatesResponse::from)
			.collect(Collectors.toList());

		return new AvailableDatesResponses(responses);
	}

}
