package io.apiorchestrationservice.api.business.service;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
public interface TokenValidationService {
	boolean validateQueueToken(String token);

}
