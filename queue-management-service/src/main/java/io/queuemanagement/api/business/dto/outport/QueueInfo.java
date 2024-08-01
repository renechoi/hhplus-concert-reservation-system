package io.queuemanagement.api.business.dto.outport;

/**
 * @author : Rene Choi
 * @since : 2024/07/31
 */
public record QueueInfo(
	 String queueName,
	 String member,
	 double score,
	 boolean success
) {
}
