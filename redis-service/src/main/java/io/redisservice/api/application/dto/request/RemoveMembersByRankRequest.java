package io.redisservice.api.application.dto.request;

import io.redisservice.api.business.dto.command.RemoveMembersByRankCommand;
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
public class RemoveMembersByRankRequest {
	private String queueName;
	private int start;
	private int end;


	public RemoveMembersByRankCommand toCommand() {
		return new RemoveMembersByRankCommand(queueName, start, end);
	}
}