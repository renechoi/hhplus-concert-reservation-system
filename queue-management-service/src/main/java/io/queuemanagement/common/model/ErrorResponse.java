package io.queuemanagement.common.model;

public record ErrorResponse(
        String code,
        String message
) {
}
