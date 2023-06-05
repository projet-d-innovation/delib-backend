package ma.enset.noteservice.dto.notemodule;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record NoteModuleRequest(
        @NotBlank
        String codeModule,
        @NotBlank
        String sessionId,
        @NotNull @DecimalMin("0") @DecimalMax("20")
        BigDecimal note,
        boolean redoublant
) {
}
