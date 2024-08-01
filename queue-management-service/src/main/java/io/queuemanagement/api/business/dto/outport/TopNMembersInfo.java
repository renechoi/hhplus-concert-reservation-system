package io.queuemanagement.api.business.dto.outport;

import java.util.Set;

/**
 * @author : Rene Choi
 * @since : 2024/07/31
 */
public record TopNMembersInfo(
	String queueName,
	Set<String> members
) {
}
