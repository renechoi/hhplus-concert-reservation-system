package io.reservationservice.api.business.dto.outport;

import java.util.Collections;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestAvailableSeatsInfos {

	private List<TestAvailableSeatsInfo> availableDateInfos;

	public static TestAvailableSeatsInfos from(List<TestAvailableSeatsInfo> availableDateInfos) {
		return new TestAvailableSeatsInfos(availableDateInfos);
	}
}
