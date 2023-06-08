package ma.enset.deliberationservice.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record GroupedNotesElementResponse(
        String sessionId,
        List<NoteElementResponse> notes
) {
}
