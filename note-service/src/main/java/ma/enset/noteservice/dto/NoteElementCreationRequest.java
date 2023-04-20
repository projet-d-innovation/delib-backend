package ma.enset.noteservice.dto;

import jakarta.validation.constraints.*;
import jakarta.ws.rs.DefaultValue;
import net.bytebuddy.implementation.bind.annotation.Default;

import java.math.BigDecimal;

public record NoteElementCreationRequest(
    @NotBlank
    String NoteElementId,
    @Min(0)
    @Max(20)
    @NotNull
    float note,
    @NotNull
    boolean redoublant,
    @NotBlank
    String codeElement

) {}
