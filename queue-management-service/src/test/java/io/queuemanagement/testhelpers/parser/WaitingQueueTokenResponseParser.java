package io.queuemanagement.testhelpers.parser;

import io.queuemanagement.api.application.dto.response.WaitingQueueTokenGeneralResponse;
import io.queuemanagement.api.application.dto.response.WaitingQueueTokenGenerationResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

/**
 * @author : Rene Choi
 * @since : 2024/07/06
 */
public class WaitingQueueTokenResponseParser {
	public static WaitingQueueTokenGenerationResponse parseWaitingQueueTokenGenerationResponse(ExtractableResponse<Response> response) {
		return response.as(WaitingQueueTokenGenerationResponse.class);
	}

	public static WaitingQueueTokenGeneralResponse parseWaitingQueueTokenGeneralResponse(ExtractableResponse<Response> response) {
		return response.as(WaitingQueueTokenGeneralResponse.class);
	}
}