package io.redisservice.api.application.dto.response;

import io.redisservice.api.business.dto.info.MemberRankInfo;
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
public class MemberRankResponse {
	private String queueName;
	private String member;
	private Integer rank;

	public static MemberRankResponse from(MemberRankInfo memberRankInfo) {
		return ObjectMapperBasedVoMapper.convert(memberRankInfo, MemberRankResponse.class);
	}
}