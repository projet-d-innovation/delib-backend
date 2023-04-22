package ma.enset.noteservice.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.DefaultValue;
import lombok.Builder;
import ma.enset.noteservice.enums.Resultat;
import net.bytebuddy.implementation.bind.annotation.Default;

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
//    @NotNull
    @DefaultValue("false")
    boolean redoublant,
//    @NotNull
    @DefaultValue("false")
    boolean rattrapage ,
    @NotBlank
    String codeModule,
    @NotBlank
    String codeSession

) {}
