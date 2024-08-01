package io.redisservice.api.business.dto.info;

/**
 * @author : Rene Choi
 * @since : 2024/07/31
 */
public record MemberRankInfo(String queueName, String member, Integer rank) {
	public static MemberRankInfo createMemberRankInfo(String queueName, String member, Integer rank) {
		return new MemberRankInfo(queueName, member, rank);
	}
}