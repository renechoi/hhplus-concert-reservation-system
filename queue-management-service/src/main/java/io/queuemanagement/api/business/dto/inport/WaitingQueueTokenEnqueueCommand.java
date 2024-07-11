package io.queuemanagement.api.business.dto.inport;

import java.time.LocalDateTime;

import io.queuemanagement.api.business.domainmodel.WaitingQueueToken;
import io.queuemanagement.api.business.dto.common.AbstractCommonRequestInfo;
import io.queuemanagement.common.mapper.ObjectMapperBasedVoMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/06
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WaitingQueueTokenEnqueueCommand extends AbstractCommonRequestInfo {
	private Long waitingQueueTokenId;
	private Long waitingQueueId;
	private  String userId;
	private  String tokenValue;
	private Long position;

	public WaitingQueueTokenEnqueueCommand withRequestAt(LocalDateTime requestAt) {
		this.setRequestAt(requestAt);
		return this;
	}

	public WaitingQueueToken toDomain() {
		return ObjectMapperBasedVoMapper.convert(this, WaitingQueueToken.class);
	}


}