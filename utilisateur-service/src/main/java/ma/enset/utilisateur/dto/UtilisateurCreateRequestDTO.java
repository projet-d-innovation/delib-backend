package ma.enset.utilisateur.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.util.List;

@Builder
public record UtilisateurCreateRequestDTO(
        @NotBlank()
        String code,
        @NotBlank()
        String nom,
        @NotBlank()
        String prenom,
        @NotBlank()
        String telephone,
        String photo,
        List<String> roles
) {
}
