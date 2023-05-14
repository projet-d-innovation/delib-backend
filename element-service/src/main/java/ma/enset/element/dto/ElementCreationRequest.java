package ma.enset.element.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ElementCreationRequest(
        @NotBlank
        String codeElement,

        @NotBlank
        String intituleElement,

        @NotNull
        @DecimalMin("0.1")
        @DecimalMax("1")
        BigDecimal coefficientElement,

        @NotBlank
        String codeModule,

        String codeProfesseur
) {
}
