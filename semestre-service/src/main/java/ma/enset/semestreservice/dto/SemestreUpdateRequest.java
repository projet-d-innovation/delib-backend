package ma.enset.semestreservice.dto;

import jakarta.validation.constraints.NotBlank;

public record SemestreUpdateRequest(
    @NotBlank
    String intituleSemestre
) {}
