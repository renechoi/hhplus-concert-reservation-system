package io.queuemanagement.api.presentation.controller;

import static io.queuemanagement.common.model.CommonApiResponse.*;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.queuemanagement.api.application.dto.request.TokenGenerateRequest;
import io.queuemanagement.api.application.dto.response.TokenGenerationResponse;
import io.queuemanagement.api.application.facade.TokenFacade;
import io.queuemanagement.common.model.CommonApiResponse;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/03
 */
@RestController
@RequestMapping("/api/token")
@RequiredArgsConstructor
public class TokenController {

	private final TokenFacade tokenFacade;

	@PostMapping
	public CommonApiResponse<TokenGenerationResponse> generateToken(@RequestBody TokenGenerateRequest tokenRequest) {
		return created(tokenFacade.generateToken(tokenRequest));
	}
}
