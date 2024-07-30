package io.redisservice.api.application.dto.request;

import io.redisservice.api.business.dto.command.GetMemberRankCommand;
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
public class GetMemberRankRequest {
	private String queueName;
	private String member;

	public GetMemberRankCommand toCommand() {
		return new GetMemberRankCommand(queueName, member);
	}
}