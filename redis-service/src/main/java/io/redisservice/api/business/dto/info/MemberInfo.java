package io.redisservice.api.business.dto.info;

/**
 * @author : Rene Choi
 * @since : 2024/07/31
 */
public record MemberInfo(String member, Long rank, Double score) {

	public static MemberInfo createMemberInfo(String member, Long rank, Double score) {
		return new MemberInfo(member, rank, score);
	}
}