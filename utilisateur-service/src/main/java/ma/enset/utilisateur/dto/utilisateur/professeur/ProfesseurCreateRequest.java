package ma.enset.utilisateur.dto.utilisateur.professeur;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.util.List;

@Builder
public record ProfesseurCreateRequest(
        @NotBlank()
        String code,
        @NotBlank()
        String nom,
        @NotBlank()
        String prenom,
        @NotBlank()
        String telephone,
        @NotBlank()
        String codeDepartement,
        String photo
) {
}
