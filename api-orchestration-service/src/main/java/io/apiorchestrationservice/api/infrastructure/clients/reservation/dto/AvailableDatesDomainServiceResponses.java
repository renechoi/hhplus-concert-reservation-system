package io.apiorchestrationservice.api.infrastructure.clients.reservation.dto;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import io.apiorchestrationservice.api.business.dto.outport.AvailableDatesInfos;

/**
 * @author : Rene Choi
 * @since : 2024/07/08
 */
public record AvailableDatesDomainServiceResponses(List<AvailableDateDomainServiceResponse> availableDateResponses) {
	public AvailableDatesDomainServiceResponses {
		availableDateResponses = availableDateResponses != null ? List.copyOf(availableDateResponses) : Collections.emptyList();
	}

	public static AvailableDatesDomainServiceResponses from(AvailableDateDomainServiceInfos availableDateDomainServiceInfos) {
		List<AvailableDateDomainServiceResponse> responses = availableDateDomainServiceInfos.availableDateDomainServiceInfos().stream()
			.map(AvailableDateDomainServiceResponse::from)
			.collect(Collectors.toList());

		return new AvailableDatesDomainServiceResponses(responses);
	}

	public AvailableDatesInfos toAvailableDatesInfo() {
		return new AvailableDatesInfos(availableDateResponses.stream()
			.map(AvailableDateDomainServiceResponse::toAvailableDateInfo)
			.collect(Collectors.toList()));
	}
}
