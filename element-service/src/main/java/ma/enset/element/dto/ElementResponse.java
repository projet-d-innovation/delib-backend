package ma.enset.element.dto;

import lombok.Builder;

@Builder
public record ElementResponse(
        String codeElement,
        String intituleElement,
        float coefficientElement,
        String codeModule,
        String codeProfesseur
) {
}
