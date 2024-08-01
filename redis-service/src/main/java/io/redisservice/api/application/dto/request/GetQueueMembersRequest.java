package io.redisservice.api.application.dto.request;

import io.redisservice.api.business.dto.command.GetQueueMembersCommand;
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
public class GetQueueMembersRequest {
	private String queueName;

	public GetQueueMembersCommand toCommand() {
		return new GetQueueMembersCommand(queueName);
	}
}