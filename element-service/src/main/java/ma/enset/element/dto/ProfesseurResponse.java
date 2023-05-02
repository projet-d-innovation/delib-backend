package ma.enset.element.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record ProfesseurResponse(
        String code,
        String nom,
        String prenom,
        String telephone,
        String photo,
        List<String> elementIds
) {
}
