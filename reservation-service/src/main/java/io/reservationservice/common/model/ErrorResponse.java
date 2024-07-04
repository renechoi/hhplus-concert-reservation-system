package io.reservationservice.common.model;

public record ErrorResponse(
        String code,
        String message
) {
}
