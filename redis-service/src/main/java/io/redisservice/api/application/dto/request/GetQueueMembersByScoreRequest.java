package io.redisservice.api.application.dto.request;

import io.redisservice.api.business.dto.command.GetQueueMembersByScoreCommand;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetQueueMembersByScoreRequest {
	private String queueName;
	private double minScore;
	private double maxScore;

	public GetQueueMembersByScoreCommand toCommand() {
		return new GetQueueMembersByScoreCommand(queueName, minScore, maxScore);
	}
}