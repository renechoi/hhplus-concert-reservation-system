package io.queuemanagement.api.business.dto.inport;

import io.queuemanagement.api.business.domainmodel.WaitingQueueToken;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/31
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnqueueCommand {
	private String queueName;
	private String member;
	private double score;

	public static EnqueueCommand waitingEnqueueCommand(WaitingQueueToken token) {
		return EnqueueCommand.builder()
			.queueName("waitingQueue")
			.member(token.getTokenValue())
			.score(System.currentTimeMillis() / 1000.0)
			.build();
	}
}
