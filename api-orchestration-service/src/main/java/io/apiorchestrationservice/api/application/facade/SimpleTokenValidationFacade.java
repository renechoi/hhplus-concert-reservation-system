package io.apiorchestrationservice.api.application.facade;

import io.apiorchestrationservice.api.business.service.TokenValidationService;
import io.apiorchestrationservice.common.annotation.Facade;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
@Facade
@RequiredArgsConstructor
public class SimpleTokenValidationFacade implements TokenValidationFacade{
	private final TokenValidationService tokenValidationService;

	@Override
	public boolean validateQueueToken(String token) {
		return tokenValidationService.validateQueueToken(token);
	}
}
