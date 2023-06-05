package ma.enset.noteservice.dto.noteelement;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record NoteElementUpdateRequest(
        @NotBlank
        String codeElement,
        @NotBlank
        String sessionId,
        @DecimalMin("0") @DecimalMax("20")
        BigDecimal note,
        boolean redoublant
) {
}
