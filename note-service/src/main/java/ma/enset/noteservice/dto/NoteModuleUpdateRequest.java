package ma.enset.noteservice.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record NoteModuleUpdateRequest(

    @NotBlank
    String noteModuleId,
    @Min(0)
    @Max(20)
    float note,
    boolean redoublant,
    boolean rattrapage
) {}
