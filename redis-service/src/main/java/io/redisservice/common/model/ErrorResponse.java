package io.redisservice.common.model;

public record ErrorResponse(
        String code,
        String message
) {
}
