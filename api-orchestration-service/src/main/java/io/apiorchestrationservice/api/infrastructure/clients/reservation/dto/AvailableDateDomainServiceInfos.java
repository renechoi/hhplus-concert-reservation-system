package io.apiorchestrationservice.api.infrastructure.clients.reservation.dto;

import java.util.Collections;
import java.util.List;

/**
 * @author : Rene Choi
 * @since : 2024/07/08
 */
public record AvailableDateDomainServiceInfos(List<AvailableDateDomainServiceInfo> availableDateDomainServiceInfos) {
	public AvailableDateDomainServiceInfos {
		availableDateDomainServiceInfos = availableDateDomainServiceInfos != null ? List.copyOf(availableDateDomainServiceInfos) : Collections.emptyList();
	}

	public static AvailableDateDomainServiceInfos from(List<AvailableDateDomainServiceInfo> availableDateDomainServiceInfos) {
		return new AvailableDateDomainServiceInfos(availableDateDomainServiceInfos);
	}
}
