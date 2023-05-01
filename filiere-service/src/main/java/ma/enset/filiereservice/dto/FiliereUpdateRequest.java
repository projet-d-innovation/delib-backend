package ma.enset.filiereservice.dto;

import jakarta.validation.constraints.NotBlank;

public record FiliereUpdateRequest(


        @NotBlank
        String intituleFiliere,
        @NotBlank
        String codeDepartement,

        @NotBlank
        String codeChefFiliere
) {
}
