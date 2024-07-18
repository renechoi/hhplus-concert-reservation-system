package io.messageservice.common.model;

public record ErrorResponse(
        String code,
        String message
) {
}
