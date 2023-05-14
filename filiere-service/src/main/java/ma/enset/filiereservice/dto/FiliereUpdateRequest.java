package ma.enset.filiereservice.dto;

import jakarta.validation.constraints.NotBlank;

public record FiliereUpdateRequest(

        String intituleFiliere,
        String codeChefFiliere,
        String codeRegleDeCalcul

) {
}
