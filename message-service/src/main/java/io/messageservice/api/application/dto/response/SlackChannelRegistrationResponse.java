package io.messageservice.api.application.dto.response;

import io.messageservice.api.business.model.dto.outport.SlackChannelRegistrationInfo;
import io.messageservice.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/07/18
 */
public record SlackChannelRegistrationResponse(
	String channelName,
	String token,
	String channelId
) {
	public static SlackChannelRegistrationResponse from(SlackChannelRegistrationInfo register) {
		return ObjectMapperBasedVoMapper.convert(register, SlackChannelRegistrationResponse.class);
	}
}
