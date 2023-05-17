package ma.enset.semestreservice.dto;

import jakarta.validation.constraints.NotBlank;

public record SemestreCreationRequest(
    @NotBlank
    String codeSemestre,

    @NotBlank
    String codeFiliere,

    @NotBlank
    String intituleSemestre
) {}
