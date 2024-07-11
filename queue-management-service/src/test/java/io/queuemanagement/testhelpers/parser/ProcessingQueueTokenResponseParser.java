package io.queuemanagement.testhelpers.parser;

import io.queuemanagement.api.application.dto.response.ProcessingQueueTokenGeneralResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
public class ProcessingQueueTokenResponseParser {

	public static ProcessingQueueTokenGeneralResponse parseProcessingQueueTokenGeneralResponse(ExtractableResponse<Response> response) {
		return response.as(ProcessingQueueTokenGeneralResponse.class);
	}
}