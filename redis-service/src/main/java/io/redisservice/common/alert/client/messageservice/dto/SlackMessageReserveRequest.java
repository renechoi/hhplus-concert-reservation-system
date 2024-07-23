package io.redisservice.common.alert.client.messageservice.dto;

import static io.redisservice.common.alert.helper.SlackMessageFormatter.*;

import io.redisservice.common.alert.GlobalExceptionAlertEvent;
import io.redisservice.util.YmlLoader;
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
