package io.apiorchestrationservice.api.application.dto.request;

import io.apiorchestrationservice.api.business.dto.inport.AvailableSeatsRetrievalCommand;
import io.apiorchestrationservice.common.mapper.ObjectMapperBasedVoMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvailableSeatsRetrievalRequest {
	private Long concertOptionId;
	private Long requestAt;

	public AvailableSeatsRetrievalCommand toCommand() {
		return ObjectMapperBasedVoMapper.convert(this, AvailableSeatsRetrievalCommand.class);
	}
}