package ma.enset.deliberationservice.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ElementResponse(
        String codeElement,
        String intituleElement,
        BigDecimal coefficientElement,
        String codeModule,
        String codeProfesseur
) {
}
