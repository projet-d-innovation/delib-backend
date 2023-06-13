package ma.enset.semestreservice.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import ma.enset.semestreservice.dto.*;
import ma.enset.semestreservice.service.SemestreService;
import org.hibernate.validator.constraints.Range;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/semestres")
@RequiredArgsConstructor
@Validated
public class SemestreController {
    private final SemestreService service;

    @PostMapping
    public ResponseEntity<SemestreResponse> save(@Valid @RequestBody SemestreCreationRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.save(request));
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<SemestreResponse>> saveAll(@RequestBody @NotEmpty List<@Valid SemestreCreationRequest> request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.saveAll(request));
    }

    @GetMapping("/exists")
    public ResponseEntity<Void> existAllByIds(@RequestParam @NotEmpty Set<@NotBlank String> codesSemestre) {
        service.existAllByIds(codesSemestre);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{codeSemestre}")
    public ResponseEntity<SemestreResponse> getById(@PathVariable String codeSemestre,
                                                    @RequestParam(defaultValue = "false") boolean includeFiliere,
                                                    @RequestParam(defaultValue = "false") boolean includeModules) {

        return ResponseEntity.ok(service.findById(codeSemestre, includeFiliere, includeModules));
    }

    @GetMapping("/bulk")
    public ResponseEntity<List<SemestreResponse>> getAllByIds(@RequestParam Set<String> codesSemestre,
                                                              @RequestParam(defaultValue = "false") boolean includeFiliere,
                                                              @RequestParam(defaultValue = "false") boolean includeModules) {

        return ResponseEntity.ok(service.findAllByIds(codesSemestre, includeFiliere, includeModules));
    }

    @GetMapping("/filiere/{codeFiliere}")
    public ResponseEntity<List<SemestreResponse>> getAllByCodeFiliere(@PathVariable String codeFiliere) {
        return ResponseEntity.ok(service.findAllByCodeFiliere(codeFiliere));
    }

    @GetMapping("/filiere/bulk")
    public ResponseEntity<List<GroupedSemestresResponse>> getAllByCodeFilieres(@RequestParam Set<String> codesFiliere) {
        return ResponseEntity.ok(service.findAllByCodesFiliere(codesFiliere));
    }

    @GetMapping
    public ResponseEntity<SemestrePagingResponse> getAll(@RequestParam(defaultValue = "0") @Min(0) int page,
                                                         @RequestParam(defaultValue = "10") @Range(min = 1, max = 100) int size,
                                                         @RequestParam(defaultValue = "false") boolean includeFiliere,
                                                         @RequestParam(defaultValue = "false") boolean includeModules) {

        return ResponseEntity.ok(service.findAll(page, size, includeFiliere, includeModules));
    }

    @PatchMapping("/{codeSemestre}")
    public ResponseEntity<SemestreResponse> update(@PathVariable String codeSemestre,
                                                   @Valid @RequestBody SemestreUpdateRequest request) {

        return ResponseEntity.ok(service.update(codeSemestre, request));
    }

    @DeleteMapping("/{codeSemestre}")
    public ResponseEntity<Void> deleteById(@PathVariable String codeSemestre) {
        service.deleteById(codeSemestre);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/bulk")
    public ResponseEntity<Void> deleteAllByIds(@RequestParam @NotEmpty Set<@NotBlank String> codesSemestre) {
        service.deleteAllByIds(codesSemestre);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/filiere/{codeFiliere}")
    public ResponseEntity<Void> deleteAllByCodeFiliere(@PathVariable String codeFiliere) {
        service.deleteAllByCodeFiliere(codeFiliere);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/filiere/bulk")
    public ResponseEntity<Void> deleteAllByCodesFiliere(@RequestBody @NotEmpty Set<@NotBlank String> codesFiliere) {
        service.deleteAllByCodesFiliere(codesFiliere);
        return ResponseEntity.noContent().build();
    }
}
