package ma.enset.utilisateur.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record EtudiantUpdateRequestDTO(
        @NotBlank()
        String code,
        String cin,
        String cne,
        String nom,
        String prenom,
        String telephone,
        String adresse,
        LocalDateTime dateNaissance,
        String ville,
        String pays,
        String photo
) {
}
