package io.redisservice.api.application.dto.request;

import io.redisservice.api.business.dto.command.GetQueueMembersByRankCommand;
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
public class GetQueueMembersByRankRequest {
	private String queueName;
	private int start;
	private int end;

	public GetQueueMembersByRankCommand toCommand() {
		return new GetQueueMembersByRankCommand(queueName, start, end);
	}
}