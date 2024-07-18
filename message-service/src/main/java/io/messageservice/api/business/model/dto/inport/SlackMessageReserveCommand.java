package io.messageservice.api.business.model.dto.inport;

import io.messageservice.api.business.model.dto.outport.SlackChannelInfo;
import io.messageservice.api.business.model.entity.SlackMessage;
import io.messageservice.common.mapper.ObjectMapperBasedVoMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SlackMessageReserveCommand {
	private String channelName;
	private String message;
	private String token;
	private String channelId;
	public SlackMessage toEntity() {
		return ObjectMapperBasedVoMapper.convert(this, SlackMessage.class);
	}

	public SlackMessageReserveCommand withChannelInfo(SlackChannelInfo slackChannelInfo) {
		this.token=slackChannelInfo.token();
		this.channelId=slackChannelInfo.channelId();
		return this;
	}
}