package io.apiorchestrationservice.api.infrastructure.clients.reservation.dto;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public record AvailableSeatsDomainServiceResponses(List<AvailableSeatsDomainServiceResponse> availableSeatsResponses) {
	public AvailableSeatsDomainServiceResponses {
		availableSeatsResponses = availableSeatsResponses != null ? List.copyOf(availableSeatsResponses) : Collections.emptyList();
	}

	public static AvailableSeatsDomainServiceResponses from(AvailableSeatsDomainServiceInfos availableSeatsDomainServiceInfos) {
		List<AvailableSeatsDomainServiceResponse> responses = availableSeatsDomainServiceInfos.availableSeatsDomainServiceInfos().stream()
			.map(AvailableSeatsDomainServiceResponse::from)
			.collect(Collectors.toList());

		return new AvailableSeatsDomainServiceResponses(responses);
	}

	public AvailableSeatsInfos toAvailableSeatsInfo() {
		return new AvailableSeatsInfos(availableSeatsResponses.stream()
			.map(AvailableSeatsDomainServiceResponse::toAvailableSeatsInfo)
			.collect(Collectors.toList()));
	}
}
