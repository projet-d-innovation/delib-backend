package ma.enset.utilisateur.dto.utilisateur;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
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
        LocalDateTime dateNaissance,
        String ville,
        String pays,
        String photo,
        String codeDepartement,
        Set<String> roles
) {
}
