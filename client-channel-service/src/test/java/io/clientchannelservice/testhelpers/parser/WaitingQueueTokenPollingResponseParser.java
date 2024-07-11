package io.clientchannelservice.testhelpers.parser;

import io.clientchannelservice.api.application.dto.response.WaitingQueueTokenPollingResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
public class WaitingQueueTokenPollingResponseParser {
	public static WaitingQueueTokenPollingResponse parseWaitingQueueTokenPollingResponse(ExtractableResponse<Response> response) {
		return response.as(WaitingQueueTokenPollingResponse.class);
	}
}