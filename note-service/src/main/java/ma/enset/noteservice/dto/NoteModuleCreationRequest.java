package ma.enset.noteservice.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ma.enset.noteservice.enums.Resultat;

import javax.xml.transform.Result;

public record NoteModuleCreationRequest(
    @NotBlank
    String NoteModuleId,
    @Min(0)
    @Max(20)
    @NotNull
    float note,

    @NotNull
    Resultat resultat,
    @NotNull
    boolean redoublant,
    @NotNull
    boolean rattrapage
//    @NotBlank
//    String codeSemestre
) {}
