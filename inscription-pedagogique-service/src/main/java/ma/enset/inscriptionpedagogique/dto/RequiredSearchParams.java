package ma.enset.inscriptionpedagogique.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record RequiredSearchParams(

    @NotBlank
    String codeFiliere,

    @NotBlank
    String codeSessionUniversitaire,

    @Positive
    int annee

) { }
