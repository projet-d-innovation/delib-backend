package ma.enset.utilisateur.dto.utilisateur;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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
        LocalDateTime dateNaissance,
        String pays,
        String ville,
        String photo,
        String codeDepartement,
        Set<String> roles

) {
}
