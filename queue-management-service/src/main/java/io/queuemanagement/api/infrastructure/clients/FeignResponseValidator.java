package io.queuemanagement.api.infrastructure.clients;

import org.springframework.http.ResponseEntity;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
public interface FeignResponseValidator {
	void validate(ResponseEntity<?> responseEntity);

	void validateWithReport(ResponseEntity<?> responseEntity);
}
