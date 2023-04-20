package ma.enset.noteservice.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record NoteModuleUpdateRequest(
    @Min(0)
    @Max(20)
    float note,
    boolean redoublant,
    boolean rattrapage
) {}
