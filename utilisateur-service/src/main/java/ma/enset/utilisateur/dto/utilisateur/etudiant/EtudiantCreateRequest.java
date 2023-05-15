package ma.enset.utilisateur.dto.utilisateur.etudiant;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record EtudiantCreateRequest(
        @NotBlank()
        String code,
        @NotBlank()
        String cin,
        @NotBlank()
        String cne,
        @NotBlank()
        String nom,
        @NotBlank()
        String prenom,
        @NotBlank()
        String telephone,
        @NotBlank()
        String adresse,
        LocalDateTime dateNaissance,
        @NotBlank()
        String ville,
        String pays,
        String photo
) {
}
