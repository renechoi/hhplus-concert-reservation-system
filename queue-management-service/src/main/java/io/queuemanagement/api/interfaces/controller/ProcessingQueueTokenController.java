package io.queuemanagement.api.interfaces.controller;

import static io.queuemanagement.common.model.CommonApiResponse.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.queuemanagement.api.application.dto.response.ProcessingQueueTokenGeneralResponse;
import io.queuemanagement.api.application.facade.ProcessingQueueFacade;
import io.queuemanagement.common.model.CommonApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
@RestController
@RequestMapping("/api/processing-queue-token")
@RequiredArgsConstructor
@Tag(name = "처리열 토큰 API")
public class ProcessingQueueTokenController {

	private final ProcessingQueueFacade processingQueueFacade;

	@GetMapping({"/check-availability/{userId}", "/check-availability"})
	@Operation(summary = "처리열 토큰 유효성 조회")
	public CommonApiResponse<ProcessingQueueTokenGeneralResponse> checkProcessingQueueTokenAvailability(
		@RequestHeader("X-Queue-Token") String tokenValue,
		@PathVariable(required = false) String userId) {
		if (userId != null) {
			return OK(processingQueueFacade.checkAvailability(tokenValue, userId));
		} else {
			return OK(processingQueueFacade.checkAvailability(tokenValue));
		}
	}


}
