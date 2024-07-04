package io.apiorchestrationservice.common.model;

public record ErrorResponse(
        String code,
        String message
) {
}
