package io.messageservice.api.application.dto.response;

import java.time.LocalDateTime;

import io.messageservice.api.business.model.dto.inport.SlackMessageSendCommand;
import io.messageservice.api.business.model.dto.outport.SlackMessageReserveInfo;
import io.messageservice.api.business.model.entity.SlackMessage;
import io.messageservice.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/07/18
 */
public record SlackMessageReserveResponse(
	Long id,
	String token,
	String channelId,
	String message,
	LocalDateTime reservedAt,
	boolean sent,
	boolean popped
) {
	public static SlackMessageReserveResponse from(SlackMessage slackMessage) {
		return ObjectMapperBasedVoMapper.convert(slackMessage, SlackMessageReserveResponse.class);
	}

	public static SlackMessageReserveResponse empty() {
		return new SlackMessageReserveResponse(null, null, null, null, null, false, false);
	}

	public static SlackMessageReserveResponse from(SlackMessageReserveInfo info) {
		return ObjectMapperBasedVoMapper.convert(info, SlackMessageReserveResponse.class);
	}

	public boolean isEmpty() {
		return this.id == null;
	}

	public SlackMessageSendCommand toCommand() {
		return new SlackMessageSendCommand(this.channelId, this.token, this.message);
	}
}
