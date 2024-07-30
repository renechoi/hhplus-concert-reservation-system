package io.redisservice.api.application.dto.request;

import io.redisservice.api.business.dto.command.DequeueCommand;
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
public class DequeueRequest {
	private String queueName;
	private String member;

	public DequeueCommand toCommand() {
		return new DequeueCommand(queueName, member);
	}
}