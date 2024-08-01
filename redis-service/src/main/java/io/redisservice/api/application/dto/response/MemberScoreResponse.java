package io.redisservice.api.application.dto.response;

import io.redisservice.api.business.dto.info.MemberScoreInfo;
import io.redisservice.common.mapper.ObjectMapperBasedVoMapper;
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
public class MemberScoreResponse {
	private String queueName;
	private String member;
	private Double score;

	public static MemberScoreResponse from(MemberScoreInfo memberScoreInfo) {
		return ObjectMapperBasedVoMapper.convert(memberScoreInfo, MemberScoreResponse.class);
	}
}