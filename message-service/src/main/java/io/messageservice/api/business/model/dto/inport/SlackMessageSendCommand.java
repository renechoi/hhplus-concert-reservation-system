package io.messageservice.api.business.model.dto.inport;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SlackMessageSendCommand {
	private String channelId;
	private String token;
	private String message;
}