package io.apiorchestrationservice.api.business.dto.outport;

import java.util.Collections;
import java.util.List;

public record AvailableDatesInfos(
   List<AvailableDateInfo> availableDateInfos
) {
	public AvailableDatesInfos {
		availableDateInfos = availableDateInfos != null ? List.copyOf(availableDateInfos) : Collections.emptyList();
	}

}
