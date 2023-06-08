package ma.enset.deliberationservice.client;

import ma.enset.deliberationservice.dto.GroupedNotesModuleResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.Set;

@HttpExchange(url = "/api/v1/notes")
public interface NoteClient {
    @GetExchange(url = "/note-module/session/{sessionId}")
    ResponseEntity<GroupedNotesModuleResponse> getNotesBySession(
            @PathVariable("sessionId") String sessionId,
            @RequestParam(name = "includeModule", defaultValue = "false") boolean includeModule,
            @RequestParam(name = "includeNoteElement", defaultValue = "false") boolean includeNoteElement
    );

    @GetExchange(url = "/note-module/session/bulk")
    ResponseEntity<Set<GroupedNotesModuleResponse>> getNotesBySessions(
            @RequestParam("sessionIdList") Set<String> sessionIdList,
            @RequestParam(name = "includeModule", defaultValue = "false") boolean includeModule,
            @RequestParam(name = "includeNoteElement", defaultValue = "false") boolean includeNoteElement
    );
}
