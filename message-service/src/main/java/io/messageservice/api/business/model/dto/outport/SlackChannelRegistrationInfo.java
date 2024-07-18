package io.messageservice.api.business.model.dto.outport;

import io.messageservice.api.business.model.entity.SlackChannel;
import io.messageservice.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/07/18
 */
public record SlackChannelRegistrationInfo(
	String channelName,
	String token,
	String channelId) {
	public static SlackChannelRegistrationInfo from(SlackChannel slackChannel) {
		return ObjectMapperBasedVoMapper.convert(slackChannel, SlackChannelRegistrationInfo.class);
	}
}
