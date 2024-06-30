package io.concertreservation.common.model;

public record ErrorResponse(
        String code,
        String message
) {
}
