package io.apiorchestrationservice.common.alert.client.messageservice.dto;

import static io.apiorchestrationservice.common.alert.helper.SlackMessageFormatter.*;

import io.apiorchestrationservice.common.alert.GlobalExceptionAlertEvent;
import io.apiorchestrationservice.util.YmlLoader;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SlackMessageReserveRequest {
	private String channelName;
	private String message;

	public static SlackMessageReserveRequest requestByGlobalExceptionAlertEvent(GlobalExceptionAlertEvent event) {
		return SlackMessageReserveRequest.builder()
			.channelName(YmlLoader.getSlackChannelName())
			.message(format(event))
			.build();
	}
}
