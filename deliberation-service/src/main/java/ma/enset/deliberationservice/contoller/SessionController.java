package ma.enset.deliberationservice.contoller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import ma.enset.deliberationservice.dto.RequiredSearchParams;
import ma.enset.deliberationservice.dto.session.SessionCreationRequest;
import ma.enset.deliberationservice.dto.session.SessionPagingResponse;
import ma.enset.deliberationservice.dto.session.SessionResponse;
import ma.enset.deliberationservice.dto.session.SessionUpdateRequest;
import ma.enset.deliberationservice.model.SessionType;
import ma.enset.deliberationservice.service.SessionService;
import org.hibernate.validator.constraints.Range;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1/session")
public class SessionController {
    private final SessionService service;

    @PostMapping
    public ResponseEntity<SessionResponse> save(@Valid @RequestBody SessionCreationRequest sessionCreationRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.save(sessionCreationRequest));
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<SessionResponse>> saveAll(@RequestBody @NotEmpty List<@Valid SessionCreationRequest> sessionCreationRequestList) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.saveAll(sessionCreationRequestList));
    }

    @GetMapping("/{idSession}")
    public ResponseEntity<SessionResponse> getById(@PathVariable("idSession") String idSession) {
        return ResponseEntity
                .ok()
                .body(service.findById(idSession));
    }

    @GetMapping("/bulk")
    public ResponseEntity<List<SessionResponse>> getAllById(@RequestParam @NotEmpty Set<@NotBlank String> idSession) {
        return ResponseEntity
                .ok()
                .body(service.findAllById(idSession));
    }

    @GetMapping
    public ResponseEntity<SessionPagingResponse> getAll(@RequestParam(defaultValue = "0") @Min(0) int page,
                                                        @RequestParam(defaultValue = "10") @Range(min = 1, max = 10) int size

    ) {
        return ResponseEntity
                .ok()
                .body(service.findAll(page, size));
    }

    @GetMapping("/search")
    public ResponseEntity<List<SessionResponse>> search(@RequestParam @NotBlank String codeFiliere,
                                                        @RequestParam(required = false) String codeSemester,
                                                        @RequestParam @NotBlank String codeSessionUniversitaire,
                                                        @RequestParam @PositiveOrZero @NotNull Integer annee,
                                                        @RequestParam(required = false) SessionType type) {
        return ResponseEntity
                .ok()
                .body(service.search(codeFiliere, codeSemester, codeSessionUniversitaire, annee, type));
    }

    @PatchMapping("/{idSession}")
    public ResponseEntity<SessionResponse> update(@PathVariable String idSession,
                                                  @Valid @RequestBody SessionUpdateRequest sessionUpdateRequest) {
        return ResponseEntity
                .ok()
                .body(service.update(idSession, sessionUpdateRequest));
    }


    @DeleteMapping("/{idSession}")
    public ResponseEntity<?> delete(@PathVariable String idSession) {
        service.deleteById(idSession);
        return ResponseEntity
                .noContent()
                .build();
    }

    @DeleteMapping("/bulk")
    public ResponseEntity<?> deleteAll(@RequestParam @NotEmpty Set<@NotBlank String> idSession) {
        service.deleteById(idSession);
        return ResponseEntity
                .noContent()
                .build();
    }

    @GetMapping("/exists")
    public ResponseEntity<Void> existsAll(@RequestParam @NotEmpty Set<@NotBlank String> codesSession) {
        service.existsAllId(codesSession);
        return ResponseEntity.noContent().build();
    }

}
