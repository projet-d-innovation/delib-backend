package ma.enset.utilisateur.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record GroupedElementsResponse(
        String codeProfesseur,
        List<ElementResponse> elements

) {
}