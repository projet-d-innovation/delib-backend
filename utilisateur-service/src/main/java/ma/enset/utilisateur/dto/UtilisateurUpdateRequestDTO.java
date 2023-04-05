package ma.enset.utilisateur.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.util.List;

@Builder
public record UtilisateurUpdateRequestDTO(
        @NotBlank()
        String code,
        String nom,
        String prenom,
        String telephone,
        String photo,
        List<String> roles
) {
}
