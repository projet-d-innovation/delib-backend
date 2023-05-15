package ma.enset.utilisateur.dto.utilisateur.professeur;

import lombok.Builder;

import java.util.List;

@Builder
public record ProfesseurUpdateRequest(
        String code,
        String nom,
        String prenom,
        String telephone,
        String photo,
        String codeDepartement
) {
}
