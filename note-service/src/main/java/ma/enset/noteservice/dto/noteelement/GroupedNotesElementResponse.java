package ma.enset.noteservice.dto.noteelement;

import lombok.Builder;

import java.util.List;
import java.util.Set;

@Builder
public record GroupedNotesElementResponse(
        String sessionId,
        List<NoteElementResponse> notes
) {
}
