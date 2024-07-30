package io.redisservice.api.application.dto.response;

import java.util.Set;

import io.redisservice.api.business.dto.info.QueueMembersInfo;
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
public class QueueMembersResponse {
	private String queueName;
	private Set<String> members;

	public static QueueMembersResponse from(QueueMembersInfo queueMembersInfo) {
		return ObjectMapperBasedVoMapper.convert(queueMembersInfo, QueueMembersResponse.class);
	}
}