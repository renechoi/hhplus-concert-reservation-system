package io.redisservice.api.application.dto.response;

import io.redisservice.api.business.dto.info.QueueInfo;
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
public class QueueResponse {
	private String queueName;
	private String member;
	private double score;
	private boolean success;
	private long size;

	public static QueueResponse from(QueueInfo queueInfo) {
		return ObjectMapperBasedVoMapper.convert(queueInfo, QueueResponse.class);
	}
}