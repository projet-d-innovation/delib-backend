package ma.enset.sessionuniversitaireservice.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import ma.enset.sessionuniversitaireservice.dto.SessionCreationRequest;
import ma.enset.sessionuniversitaireservice.dto.SessionPagingResponse;
import ma.enset.sessionuniversitaireservice.dto.SessionResponse;
import ma.enset.sessionuniversitaireservice.dto.SessionUpdateRequest;
import ma.enset.sessionuniversitaireservice.service.SessionUniversitaireService;
import org.hibernate.validator.constraints.Range;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/sessions-universitaire")
@AllArgsConstructor
@Validated
public class SessionUniversitaireController {
    private final SessionUniversitaireService service;

    @PostMapping
    public ResponseEntity<SessionResponse> save(@Valid @RequestBody SessionCreationRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.save(request));
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<SessionResponse>> saveAll(@RequestBody @NotEmpty List<@NotNull @Valid SessionCreationRequest> request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.saveAll(request));
    }

    @GetMapping("/exists")
    public ResponseEntity<Void> existAllByIds(@RequestParam @NotEmpty Set<@NotBlank String> ids) {
        service.existAllByIds(ids);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SessionResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/bulk")
    public ResponseEntity<List<SessionResponse>> getAllByIds(@RequestParam Set<String> ids) {
        return ResponseEntity.ok(service.findAllByIds(ids));
    }

    @GetMapping
    public ResponseEntity<SessionPagingResponse> getAll(@RequestParam(defaultValue = "0") @Min(0) int page,
                                                        @RequestParam(defaultValue = "10") @Range(min = 1, max = 100) int size) {

        return ResponseEntity.ok(service.findAll(page, size));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SessionResponse> update(@PathVariable String id,
                                                  @Valid @RequestBody SessionUpdateRequest request) {

        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable String id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/bulk")
    public ResponseEntity<Void> deleteAllByIds(@RequestBody @NotEmpty Set<@NotBlank String> ids) {
        service.deleteAllByIds(ids);
        return ResponseEntity.noContent().build();
    }
}
