package io.messageservice.api.business.model.dto.outport;

import java.time.LocalDateTime;

import io.messageservice.api.business.model.dto.inport.SlackMessageSendCommand;
import io.messageservice.api.business.model.entity.SlackMessage;
import io.messageservice.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/07/18
 */
public record SlackMessageReserveInfo(
	Long id,
	String token,
	String channelId,
	String message,
	LocalDateTime reservedAt,
	boolean sent,
	boolean popped
) {
	public static SlackMessageReserveInfo from(SlackMessage slackMessage) {
		return ObjectMapperBasedVoMapper.convert(slackMessage, SlackMessageReserveInfo.class);
	}

	public static SlackMessageReserveInfo empty() {
		return new SlackMessageReserveInfo(null, null, null, null, null, false, false);
	}

	public boolean isEmpty() {
		return this.id == null;
	}

	public SlackMessageSendCommand toCommand() {
		return new SlackMessageSendCommand(this.channelId, this.token, this.message);
	}


}
