package ma.enset.filiereservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record FiliereCreationRequest(
        @NotBlank()
        String codeFiliere,
        @NotBlank()
        String intituleFiliere,
        @NotBlank()
        String codeDepartement,
        String codeChefFiliere,
        String codeRegleDeCalcul

) {
}
