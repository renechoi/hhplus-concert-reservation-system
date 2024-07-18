package io.messageservice.api.application.dto.request;

import io.messageservice.api.business.model.dto.inport.SlackChannelRegistrationCommand;
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
public class SlackChannelRegistrationRequest {
	@NotEmpty
	private String channelName;

	@NotEmpty
	private String token;

	@NotEmpty
	private String channelId;

	public SlackChannelRegistrationCommand toCommand() {
		return ObjectMapperBasedVoMapper.convert(this, SlackChannelRegistrationCommand.class);
	}
}
