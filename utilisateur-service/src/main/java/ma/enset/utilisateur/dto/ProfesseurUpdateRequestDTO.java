package ma.enset.utilisateur.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record ProfesseurUpdateRequestDTO(
        String code,
        String nom,
        String prenom,
        String telephone,
        String photo,
        String codeDepartement,
        List<String> elementIds
) {
}
