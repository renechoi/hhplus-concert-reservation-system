package io.apiorchestrationservice.api.business.dto.inport;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

import io.apiorchestrationservice.api.infrastructure.clients.reservation.dto.AbstractCommonRequestInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConcertOptionCreateCommand  extends AbstractCommonRequestInfo {
    private Long concertId;
    private LocalDateTime concertDate;
    private Duration concertDuration;
    private String title;
    private String description;
    private BigDecimal price;
    private int maxSeats;

    public ConcertOptionCreateCommand withConcertId(Long concertId) {
        this.concertId = concertId;
        return this;
    }
}
