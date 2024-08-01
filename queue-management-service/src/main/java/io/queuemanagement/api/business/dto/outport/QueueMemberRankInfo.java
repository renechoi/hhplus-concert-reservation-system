package io.queuemanagement.api.business.dto.outport;

/**
 * @author : Rene Choi
 * @since : 2024/07/31
 */
public record QueueMemberRankInfo(
	 String queueName,
	 String member,
	 int rank
) {
}
