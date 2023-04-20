package ma.enset.filiereservice.dto;

import jakarta.validation.constraints.NotBlank;

public record FiliereCreationRequest(
        @NotBlank()
        String codeFiliere,
        @NotBlank()
        String intituleFiliere,
        @NotBlank()
        String codeDepartement,
        @NotBlank
        String codeChefFiliere ,
        @NotBlank()
        String codeRegleDeCalcul

) {}
