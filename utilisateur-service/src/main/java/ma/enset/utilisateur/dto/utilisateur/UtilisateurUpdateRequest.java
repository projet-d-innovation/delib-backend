package ma.enset.utilisateur.dto.utilisateur;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record UtilisateurUpdateRequest(
        String code,
        String cin,
        String cne,
        String nom,
        String prenom,
        String telephone,
        String adresse,
        LocalDate dateNaissance,
        String ville,
        @Size(max = 1)
        String sexe,
        String pays,
        String photo,
        String codeDepartement,
        Set<String> roles
) {
}
