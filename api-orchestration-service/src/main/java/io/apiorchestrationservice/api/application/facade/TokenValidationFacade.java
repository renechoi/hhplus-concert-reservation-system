package io.apiorchestrationservice.api.application.facade;

/**
 * @author : Rene Choi
 * @since : 2024/07/11
 */
public interface TokenValidationFacade {
	boolean validateQueueToken(String token);
}
