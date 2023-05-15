package ma.enset.utilisateur.dto.utilisateur.administrateur;

import lombok.Builder;

import java.util.List;

@Builder
public record UtilisateurUpdateRequest(
        String code,
        String nom,
        String prenom,
        String telephone,
        String photo,
        String codeDepartement,
        String codeFiliere,
        List<String> roles
) {
}
