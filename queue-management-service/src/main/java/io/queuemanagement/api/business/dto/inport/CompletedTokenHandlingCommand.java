package io.queuemanagement.api.business.dto.inport;

import java.util.List;

import io.queuemanagement.api.business.domainmodel.ProcessingQueueToken;
import io.queuemanagement.common.mapper.ObjectMapperBasedVoMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/08/01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompletedTokenHandlingCommand {
	private String userId;

	public ProcessingQueueToken toProcessingQueueToken() {
		return ObjectMapperBasedVoMapper.convert(this, ProcessingQueueToken.class);
	}
}
