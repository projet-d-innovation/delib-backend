package ma.enset.element.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record ElementByCodeProfesseurResponse(
        String codeProfesseur,
        List<ElementResponse> elements

) {
}
