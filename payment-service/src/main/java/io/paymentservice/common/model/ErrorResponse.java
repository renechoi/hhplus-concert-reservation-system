package io.paymentservice.common.model;

public record ErrorResponse(
        String code,
        String message
) {
}
