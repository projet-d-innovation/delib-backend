package ma.enset.deliberationservice.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record GroupedNotesModuleResponse(
        String sessionId,
        List<NoteModuleResponse> notes
) {
}
