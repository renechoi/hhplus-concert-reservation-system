package io.queuemanagement.api.application.facade;

import static io.queuemanagement.api.infrastructure.entity.WaitingQueueTokenEntity.*;

import io.queuemanagement.api.application.dto.request.TokenGenerateRequest;
import io.queuemanagement.api.application.dto.response.TokenGenerationResponse;
import io.queuemanagement.api.business.service.TokenService;
import io.queuemanagement.common.annotation.Facade;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */
@Facade
@RequiredArgsConstructor
public class TokenFacade {
	private final TokenService tokenService;
	public TokenGenerationResponse generateToken(TokenGenerateRequest tokenRequest) {
		return TokenGenerationResponse.from(createMockData(tokenRequest.getUserId()));
	}
}
