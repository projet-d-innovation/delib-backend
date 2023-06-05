package ma.enset.deliberationservice.dto.session;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import ma.enset.deliberationservice.model.SessionResult;

import java.math.BigDecimal;

public record SessionUpdateRequest(
        SessionResult sessionResult,
        @Min(0) @Max(20) BigDecimal note
) {
}
