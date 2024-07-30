package io.redisservice.api.application.dto.request;

import io.redisservice.api.business.dto.command.EnqueueCommand;
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
public class EnqueueRequest {
	private String queueName;
	private String member;
	private double score;

	public EnqueueCommand toCommand() {
		return new EnqueueCommand(queueName, member, score);
	}
}
