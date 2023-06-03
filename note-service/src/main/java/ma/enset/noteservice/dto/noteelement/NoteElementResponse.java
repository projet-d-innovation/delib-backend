package ma.enset.noteservice.dto.noteelement;

import lombok.*;

import java.math.BigDecimal;

@Builder
public record NoteElementResponse(
        String codeElement,
        String sessionId,
        BigDecimal note,
        boolean redoublant
) {
}