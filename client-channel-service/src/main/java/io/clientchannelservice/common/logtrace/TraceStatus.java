package io.clientchannelservice.common.logtrace;

import lombok.AllArgsConstructor;
import lombok.Getter;
/**
 * @author : Rene Choi
 * @since : 2024/06/16
 */

@Getter
@AllArgsConstructor
public class TraceStatus {

	private TraceId traceId;
	private Long startTimeMs;
	private String message;
}
