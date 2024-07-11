package io.queuemanagement.api.presentation.controller;

import static io.queuemanagement.common.model.CommonApiResponse.*;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.queuemanagement.api.application.dto.request.WaitingQueueTokenGenerateRequest;
import io.queuemanagement.api.application.dto.response.WaitingQueueTokenGeneralResponse;
import io.queuemanagement.api.application.dto.response.WaitingQueueTokenGenerationResponse;
import io.queuemanagement.api.application.facade.WaitingQueueManagementFacade;
import io.queuemanagement.common.model.CommonApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/03
 */
@RestController
@RequestMapping("/api/waiting-queue-token")
@RequiredArgsConstructor
@Tag(name = "대기열 토큰 API")
public class WaitingQueueTokenController {

	private final WaitingQueueManagementFacade waitingQueueManagementFacade;

	@PostMapping
	@Operation(summary = "대기열 토큰 생성 및 인입")
	public CommonApiResponse<WaitingQueueTokenGenerationResponse> generateAndEnqueue(@RequestBody @Validated WaitingQueueTokenGenerateRequest tokenRequest) {
		return created(waitingQueueManagementFacade.generateAndEnqueue(tokenRequest));
	}

	@GetMapping("/{userId}")
	@Operation(summary = "대기열 토큰 정보 조회")
	public CommonApiResponse<WaitingQueueTokenGeneralResponse> retrieveWaitingQueueToken(@PathVariable String userId) {
		return OK(waitingQueueManagementFacade.retrieveToken(userId));
	}
}
