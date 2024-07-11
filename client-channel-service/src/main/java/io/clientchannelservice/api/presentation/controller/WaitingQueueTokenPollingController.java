package io.clientchannelservice.api.presentation.controller;

import static io.clientchannelservice.common.model.CommonApiResponse.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.clientchannelservice.api.application.dto.response.WaitingQueueTokenPollingResponse;
import io.clientchannelservice.api.application.facade.WaitingQueueTokenPollingFacade;
import io.clientchannelservice.common.model.CommonApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
@RestController
@RequestMapping("/api/polling")
@RequiredArgsConstructor
@Tag(name = "대기열 토큰 폴링 API")
public class WaitingQueueTokenPollingController {

	private final WaitingQueueTokenPollingFacade facade;

	@GetMapping("/{userId}")
	@Operation(summary = "대기열 토큰 정보 조회 - 폴링")
	public CommonApiResponse<WaitingQueueTokenPollingResponse> retrieveWaitingQueueToken(@PathVariable String userId) {
		return OK(facade.retrieveToken(userId));
	}



}
