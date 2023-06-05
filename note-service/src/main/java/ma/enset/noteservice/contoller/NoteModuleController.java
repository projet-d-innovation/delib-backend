package ma.enset.noteservice.contoller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import ma.enset.noteservice.dto.notemodule.GroupedNotesModuleResponse;
import ma.enset.noteservice.service.NoteModuleService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@Validated
@AllArgsConstructor
@RequestMapping("/api/v1/notes/note-module")
public class NoteModuleController {
    private final NoteModuleService noteService;


    @GetMapping("/session/{sessionId}")
    public ResponseEntity<GroupedNotesModuleResponse> getNotesBySession(
            @PathVariable @NotBlank String sessionId,
            @RequestParam(defaultValue = "false") boolean includeModule,
            @RequestParam(defaultValue = "false") boolean includeNoteElement
    ) {
        return ResponseEntity.ok(
                noteService.getNotesBySession(sessionId, includeModule, includeNoteElement)
        );
    }

    @GetMapping("/session/bulk")
    public ResponseEntity<Set<GroupedNotesModuleResponse>> getNotesBySessions(
            @RequestParam @NotEmpty Set<@NotBlank String> sessionIdList,
            @RequestParam(defaultValue = "false") boolean includeModule,
            @RequestParam(defaultValue = "false") boolean includeNoteElement
    ) {
        return ResponseEntity.ok(
                noteService.getNotesBySessions(sessionIdList, includeModule, includeNoteElement)
        );
    }


}
