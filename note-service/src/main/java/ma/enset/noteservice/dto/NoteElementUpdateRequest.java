package ma.enset.noteservice.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record NoteElementUpdateRequest(
    @Min(0)
    @Max(20)
    float note,
    boolean redoublant
//    @NotBlank
//    String codeElement
) {}
