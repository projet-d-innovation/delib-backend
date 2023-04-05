package ma.enset.element.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record ElementRequestDTO(
        @NotBlank()
        String codeElement,
        String intituleElement,

        @DecimalMin("0.1")
        @DecimalMax("0.9")
        float coefficientElement,
        @NotBlank()
        String codeModule,
        String codeProfesseur
) {
}
