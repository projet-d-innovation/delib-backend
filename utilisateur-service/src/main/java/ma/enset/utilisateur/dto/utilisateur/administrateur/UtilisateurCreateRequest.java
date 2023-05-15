package ma.enset.utilisateur.dto.utilisateur.administrateur;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.util.List;

@Builder
public record UtilisateurCreateRequest(
        @NotBlank()
        String code,
        @NotBlank()
        String nom,
        @NotBlank()
        String prenom,
        @NotBlank()
        String telephone,
        String photo,
        String codeDepartement,
        String codeFiliere,
        List<String> roles
) {
}
