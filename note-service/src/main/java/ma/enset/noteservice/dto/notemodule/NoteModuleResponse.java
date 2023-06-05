package ma.enset.noteservice.dto.notemodule;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record NoteModuleResponse(
        String codeModule,
        String sessionId,
        BigDecimal note,
        boolean redoublant
) {
}