package ma.enset.noteservice.contoller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import ma.enset.noteservice.dto.noteelement.GroupedNotesElementResponse;
import ma.enset.noteservice.dto.noteelement.NoteElementCreationRequest;
import ma.enset.noteservice.dto.noteelement.NoteElementResponse;
import ma.enset.noteservice.dto.noteelement.NoteElementUpdateRequest;
import ma.enset.noteservice.service.NoteElementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@Validated
@AllArgsConstructor
@RequestMapping("/api/v1/notes/note-element")
public class NoteElementController {
    private final NoteElementService noteService;

    @PostMapping
    public ResponseEntity<NoteElementResponse> save(@Valid @RequestBody NoteElementCreationRequest noteCreationRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(noteService.save(noteCreationRequest));
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<NoteElementResponse>> saveAll(@RequestBody @NotEmpty List<@NotNull @Valid NoteElementCreationRequest> noteCreationRequestList) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(noteService.saveAll(noteCreationRequestList));
    }

    @PatchMapping
    public ResponseEntity<NoteElementResponse> update(
            @Valid @RequestBody NoteElementUpdateRequest noteUpdateRequest
    ) {
        return ResponseEntity.ok(
                noteService.update(noteUpdateRequest)
        );
    }

    @PatchMapping("/bulk")
    public ResponseEntity<List<NoteElementResponse>> update(
            @Valid @RequestBody Set<@NotNull NoteElementUpdateRequest> noteUpdateRequestList
    ) {
        return ResponseEntity.ok(
                noteService.updateAll(noteUpdateRequestList)
        );
    }

    @GetMapping("/session/{sessionId}")
    public ResponseEntity<GroupedNotesElementResponse> getNotesBySession(
            @PathVariable @NotBlank String sessionId,
            @RequestParam(defaultValue = "false") boolean includeElement
    ) {
        return ResponseEntity.ok(
                noteService.getNotesBySession(sessionId, includeElement)
        );
    }

    @GetMapping("/session/bulk")
    public ResponseEntity<Set<GroupedNotesElementResponse>> getNotesBySessions(
            @RequestParam @NotEmpty Set<@NotBlank String> sessionIdList,
            @RequestParam(defaultValue = "false") boolean includeElement
    ) {
        return ResponseEntity.ok(
                noteService.getNotesBySessions(sessionIdList, includeElement)
        );
    }


    @DeleteMapping("/session/{sessionId}")
    public ResponseEntity<Void> delete(
            @PathVariable @NotBlank String sessionId
    ) {
        noteService.deleteBySession(sessionId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/session/bulk")
    public ResponseEntity<Void> delete(
            @RequestParam @NotEmpty Set<@NotBlank String> sessionIdList
    ) {
        noteService.deleteAllBySession(sessionIdList);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/session/{sessionId}/element/{codeElement}")
    public ResponseEntity<Void> delete(
            @PathVariable @NotBlank String sessionId,
            @PathVariable @NotBlank String codeElement
    ) {
        noteService.deleteBySessionAndElement(sessionId, codeElement);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/session/{sessionId}/element/bulk")
    public ResponseEntity<Void> delete(
            @PathVariable @NotBlank String sessionId,
            @RequestParam @NotEmpty Set<@NotBlank String> codeElementList
    ) {
        noteService.deleteAllBySessionAndElement(sessionId, codeElementList);
        return ResponseEntity.noContent().build();
    }

}
