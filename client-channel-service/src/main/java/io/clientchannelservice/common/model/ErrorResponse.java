package io.clientchannelservice.common.model;

public record ErrorResponse(
        String code,
        String message
) {
}
