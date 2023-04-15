package ma.enset.utilisateur.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record ProfesseurResponseDTO(
        String code,
        String nom,
        String prenom,
        String telephone,
        String photo,
        String codeDepartement,
        DepartementResponseDTO departement,
        List<String> elementIds,
        List<Object> elements
) {
}
