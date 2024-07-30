package io.redisservice.api.business.dto.info;

/**
 * @author : Rene Choi
 * @since : 2024/07/31
 */
public record MemberScoreInfo(String queueName, String member, Double score) {
	public static MemberScoreInfo createMemberScoreInfo(String queueName, String member, Double score) {
		return new MemberScoreInfo(queueName, member, score);
	}
}