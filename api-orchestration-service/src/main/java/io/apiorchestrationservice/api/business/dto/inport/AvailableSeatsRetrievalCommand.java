package io.apiorchestrationservice.api.business.dto.inport;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvailableSeatsRetrievalCommand {
	private Long concertOptionId;
	private Long requestAt;
}
