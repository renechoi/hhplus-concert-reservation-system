package io.apiorchestrationservice.api.business.dto.inport;

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
public class ConcertCreateCommand extends AbstractCommonRequestInfo {
    private String title;
}
