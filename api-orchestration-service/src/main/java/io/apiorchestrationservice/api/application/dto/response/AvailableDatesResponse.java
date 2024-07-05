package io.apiorchestrationservice.api.application.dto.response;

import java.time.LocalDate;
import java.util.List;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */
public record AvailableDatesResponse(Long concertId, List<LocalDate> availableDates) {
}
