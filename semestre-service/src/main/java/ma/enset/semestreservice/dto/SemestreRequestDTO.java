package ma.enset.semestreservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record SemestreRequestDTO(
        @NotBlank
        String codeSemestre,
        @NotBlank
        String codeFiliere,
        @NotBlank
        String intituleSemestre
) {
}
