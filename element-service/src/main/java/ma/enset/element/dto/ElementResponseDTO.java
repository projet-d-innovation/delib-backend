package ma.enset.element.dto;

public record ElementResponseDTO(
        String codeElement,
        String intituleElement,
        float coefficientElement,
        String codeModule,
        String codeProfesseur
) {
}
