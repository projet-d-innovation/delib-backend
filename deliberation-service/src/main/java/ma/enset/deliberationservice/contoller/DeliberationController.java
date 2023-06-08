package ma.enset.deliberationservice.contoller;

import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import ma.enset.deliberationservice.dto.session.SessionResponse;
import ma.enset.deliberationservice.model.SessionType;
import ma.enset.deliberationservice.service.DeliberationService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1/deliberation")
public class DeliberationController {
    private final DeliberationService service;

    @GetMapping
    public ResponseEntity<List<SessionResponse>> search(@RequestParam @NotBlank String codeFiliere,
                                                        @RequestParam @NotBlank String codeSemester,
                                                        @RequestParam @NotBlank String codeSessionUniversitaire,
                                                        @RequestParam @PositiveOrZero @NotNull Integer annee,
                                                        @RequestParam @NotNull SessionType type
    ) {
        return ResponseEntity
                .ok()
                .body(service.get(codeFiliere, codeSemester, codeSessionUniversitaire, annee, type));
    }

}
