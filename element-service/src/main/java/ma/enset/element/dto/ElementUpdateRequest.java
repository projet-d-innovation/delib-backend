package ma.enset.element.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ElementUpdateRequest(
    String intituleElement,

    @DecimalMin("0.1")
    @DecimalMax("1")
    BigDecimal coefficientElement,

    String codeProfesseur
) {
}
