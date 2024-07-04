package io.balanceservice.common.model;

public record ErrorResponse(
        String code,
        String message
) {
}
