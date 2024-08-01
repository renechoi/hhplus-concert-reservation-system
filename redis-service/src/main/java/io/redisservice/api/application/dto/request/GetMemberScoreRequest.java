package io.redisservice.api.application.dto.request;

import io.redisservice.api.business.dto.command.GetMemberScoreCommand;
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
public class GetMemberScoreRequest {
	private String queueName;
	private String member;

	public GetMemberScoreCommand toCommand() {
		return new GetMemberScoreCommand(queueName, member);
	}
}