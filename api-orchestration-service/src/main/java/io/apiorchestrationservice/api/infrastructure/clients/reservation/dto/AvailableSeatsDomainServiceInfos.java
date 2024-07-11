package io.apiorchestrationservice.api.infrastructure.clients.reservation.dto;

import java.util.Collections;
import java.util.List;

/**
 * @author : Rene Choi
 * @since : 2024/07/08
 */
public record AvailableSeatsDomainServiceInfos(List<AvailableSeatsDomainServiceInfo> availableSeatsDomainServiceInfos) {
	public AvailableSeatsDomainServiceInfos {
		availableSeatsDomainServiceInfos = availableSeatsDomainServiceInfos != null ? List.copyOf(availableSeatsDomainServiceInfos) : Collections.emptyList();
	}

	public static AvailableSeatsDomainServiceInfos from(List<AvailableSeatsDomainServiceInfo> availableDateInfos) {
		return new AvailableSeatsDomainServiceInfos(availableDateInfos);
	}
}
