package io.messageservice.api.application.dto.request;

import io.messageservice.api.business.model.dto.inport.SlackMessageReserveCommand;
import io.messageservice.api.business.model.dto.inport.SlackMessageSendCommand;
import io.messageservice.common.mapper.ObjectMapperBasedVoMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SlackMessageSendRequest {
	private String channelName;
	private String message;

	public SlackMessageSendCommand toCommand() {
		return ObjectMapperBasedVoMapper.convert(this, SlackMessageSendCommand.class);
	}
}