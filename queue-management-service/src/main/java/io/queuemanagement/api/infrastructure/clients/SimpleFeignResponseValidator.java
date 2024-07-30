package io.queuemanagement.api.infrastructure.clients;


import static io.queuemanagement.common.model.GlobalResponseCode.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import io.queuemanagement.common.exception.definitions.FeignCommunicationException;
import io.queuemanagement.common.exception.definitions.ItemNotFoundException;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */

@Component
@RequiredArgsConstructor
public class SimpleFeignResponseValidator implements FeignResponseValidator {

	@Override
	public void validate(ResponseEntity<?> responseEntity) {
		HttpStatus statusCode = (HttpStatus) responseEntity.getStatusCode();

		if (!statusCode.is2xxSuccessful()) {
			throw new FeignCommunicationException(FEIGN_EXCHANGE_EXCEPTION, statusCode.value() + " " + statusCode.getReasonPhrase());
		}

		if (statusCode.value() == 204){
			throw new ItemNotFoundException(FEIGN_EXCHANGE_EXCEPTION, statusCode.value() + " " + statusCode.getReasonPhrase());
		}
	}

	@Override
	public void validateWithReport(ResponseEntity<?> responseEntity) {
		HttpStatus statusCode = (HttpStatus) responseEntity.getStatusCode();

		if (!statusCode.is2xxSuccessful()) {
			throw new FeignCommunicationException(FEIGN_EXCHANGE_EXCEPTION, statusCode.value() + " " + statusCode.getReasonPhrase());
		}
	}
}
