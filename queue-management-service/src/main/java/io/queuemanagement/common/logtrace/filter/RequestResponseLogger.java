package io.queuemanagement.common.logtrace.filter;

import io.queuemanagement.common.logtrace.TraceId;
import io.queuemanagement.common.logtrace.TraceStatus;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author : Rene Choi
 * @since : 2024/07/17
 */
@Slf4j
public class RequestResponseLogger {

    public TraceStatus startTrace(CachedBodyHttpServletRequest request)  {
        TraceId traceId = new TraceId();
        Long startTimeMs = System.currentTimeMillis();
        String message = request.getMethod() + " " + request.getRequestURI();
        String requestBody = request.getCachedBodyAsString();
        log.info("[{}] --> Request: {} Body: {}", traceId.getId(), message, requestBody);
        return new TraceStatus(traceId, startTimeMs, message);
    }

    public void endTrace(TraceStatus status, CachedBodyHttpServletResponse response) {
        Long endTimeMs = System.currentTimeMillis();
        long duration = endTimeMs - status.getStartTimeMs();
        log.info("[{}] <-- Response: status={} duration={}ms", status.getTraceId().getId(), response.getStatus(), duration);
    }

    public void logResponseBody(CachedBodyHttpServletResponse response) throws IOException {
        String responseBody = response.getCachedBodyAsString();
        log.info("Response Body: {}", responseBody);
    }

    public void logException(TraceStatus status, Exception e) {
        Long endTimeMs = System.currentTimeMillis();
        long duration = endTimeMs - status.getStartTimeMs();
        log.error("[{}] <X- Exception: message={} duration={}ms exception={}", status.getTraceId().getId(), status.getMessage(), duration, e.getMessage());
    }
}



