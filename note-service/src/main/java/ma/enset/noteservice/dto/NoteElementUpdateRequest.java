package ma.enset.noteservice.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record NoteElementUpdateRequest(
    @NotBlank
    String noteElementId,
    @Min(0)
    @Max(20)
    float note,
    boolean redoublant
) {}
