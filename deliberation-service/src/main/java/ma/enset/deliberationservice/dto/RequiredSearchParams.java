package ma.enset.deliberationservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record RequiredSearchParams(

        @NotBlank
        String codeFiliere,

        @NotBlank
        String codeSessionUniversitaire,

        @NotNull
        @Positive
        Integer annee

) {
}
