package io.messageservice.api.business.model.dto.outport;

import io.messageservice.api.business.model.entity.SlackChannel;
import io.messageservice.common.mapper.ObjectMapperBasedVoMapper;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * @author : Rene Choi
 * @since : 2024/07/18
 */
public record SlackChannelInfo(
	 Long id,
	 String channelName,
	 String token,
	 String channelId
) {
	public static SlackChannelInfo from(SlackChannel slackChannel) {
		return ObjectMapperBasedVoMapper.convert(slackChannel, SlackChannelInfo.class);
	}
}
