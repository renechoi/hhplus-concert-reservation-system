package io.redisservice.api.application.dto.request;

import io.redisservice.api.business.dto.command.GetQueueMembersByScoreCommand;
import io.redisservice.api.business.dto.command.RemoveMembersByScoreCommand;
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
public class RemoveMembersByScoreRequest {
	private String queueName;
	private double minScore;
	private double maxScore;



	public RemoveMembersByScoreCommand toCommand() {
		return new RemoveMembersByScoreCommand(queueName, minScore, maxScore);
	}
}