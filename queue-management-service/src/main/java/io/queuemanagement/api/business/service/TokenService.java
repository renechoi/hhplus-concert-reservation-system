package io.queuemanagement.api.business.service;

import io.queuemanagement.api.business.domainmodel.WaitingQueueToken;
import io.queuemanagement.api.business.dto.inport.TokenGenerateCommand;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */
public interface TokenService {
	WaitingQueueToken generateToken(TokenGenerateCommand tokenGenerateCommand);
}
