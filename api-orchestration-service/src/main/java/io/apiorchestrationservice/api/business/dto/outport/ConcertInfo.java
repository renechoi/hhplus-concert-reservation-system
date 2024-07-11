package io.apiorchestrationservice.api.business.dto.outport;

import java.time.LocalDateTime;

public record ConcertInfo(
    Long concertId,
    String title,
    LocalDateTime createdAt,
    LocalDateTime requestAt
) {}
