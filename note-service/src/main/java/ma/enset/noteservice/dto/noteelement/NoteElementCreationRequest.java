package ma.enset.noteservice.dto.noteelement;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record NoteElementCreationRequest(
        @NotBlank
        String codeElement,
        @NotBlank
        String sessionId,
        @NotNull @DecimalMin("0") @DecimalMax("20")
        BigDecimal note,
        boolean redoublant
) {
}
