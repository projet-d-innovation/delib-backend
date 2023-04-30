package ma.enset.noteservice.controller;

import lombok.AllArgsConstructor;
import ma.enset.noteservice.service.NoteYearService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notes/years")
@AllArgsConstructor
@Validated
public class NoteYearController {


private final NoteYearService noteYearService;

    @GetMapping("/{codeYear}")
    public ResponseEntity<?> getNoteSemesterByCodeSession(@PathVariable("codeYear") String codeYear) {
        float noteYear  = noteYearService.calculateNoteByCodeYear(codeYear);

        return ResponseEntity
                .ok()
                .body(noteYear);
    }

}

