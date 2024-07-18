package io.messageservice.api.business.model.dto.inport;

import com.slack.api.Slack;

import io.messageservice.api.business.model.entity.SlackChannel;
import io.messageservice.common.mapper.ObjectMapperBasedVoMapper;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SlackChannelRegistrationCommand {
	private String channelName;
	private String token;
	private String channelId;

	public SlackChannel toEntity() {
		return ObjectMapperBasedVoMapper.convert(this, SlackChannel.class);
	}
}
