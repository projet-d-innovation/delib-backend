package ma.enset.noteservice.dto.notemodule;

import lombok.Builder;

import java.util.List;

@Builder
public record GroupedNotesModuleResponse(
        String sessionId,
        List<NoteModuleResponse> notes
) {
}
