package ma.enset.filiereservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record FiliereRequestDTO(
        @NotBlank()
        String codeFiliere,
        @NotBlank()
        String intituleFiliere,

        @NotBlank()
        String codeRegleDeCalcul,
        @NotBlank()
        String codeDepartement
) {
}
