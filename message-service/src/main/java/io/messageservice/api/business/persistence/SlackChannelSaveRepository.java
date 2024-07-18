package io.messageservice.api.business.persistence;

import io.messageservice.api.business.model.entity.SlackChannel;

/**
 * @author : Rene Choi
 * @since : 2024/07/18
 */
public interface SlackChannelSaveRepository {
	SlackChannel save(SlackChannel slackChannel);
}
