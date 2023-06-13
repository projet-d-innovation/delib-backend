package ma.enset.moduleservice.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import ma.enset.moduleservice.dto.*;
import ma.enset.moduleservice.service.ModuleService;
import org.hibernate.validator.constraints.Range;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/modules")
@RequiredArgsConstructor
@Validated
public class ModuleController {
    private final ModuleService service;

    @PostMapping
    public ResponseEntity<ModuleResponse> save(@Valid @RequestBody ModuleCreationRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.save(request));
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<ModuleResponse>> saveAll(@RequestBody @NotEmpty List<@Valid ModuleCreationRequest> request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.saveAll(request));
    }

    @GetMapping("/exists")
    public ResponseEntity<Void> existAllByIds(@RequestParam @NotEmpty Set<@NotBlank String> codesModule) {
        service.existAllByIds(codesModule);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{codeModule}")
    public ResponseEntity<ModuleResponse> getById(@PathVariable String codeModule,
                                                  @RequestParam(defaultValue = "false") boolean includeSemestre,
                                                  @RequestParam(defaultValue = "false") boolean includeElements) {

        return ResponseEntity.ok(service.findById(codeModule, includeSemestre, includeElements));
    }

    @GetMapping("/bulk")
    public ResponseEntity<List<ModuleResponse>> getAllByIds(@RequestParam Set<String> codesModule,
                                                            @RequestParam(defaultValue = "false") boolean includeSemestre,
                                                            @RequestParam(defaultValue = "false") boolean includeElements) {

        return ResponseEntity.ok(service.findAllByIds(codesModule, includeSemestre, includeElements));
    }

    @GetMapping("/semestre/{codeSemestre}")
    public ResponseEntity<List<ModuleResponse>> getAllByCodeSemestre(@PathVariable String codeSemestre) {
        return ResponseEntity.ok(service.findAllByCodeSemestre(codeSemestre));
    }

    @GetMapping("/semestre/bulk")
    public ResponseEntity<List<GroupedModulesResponse>> getAllByCodeSemestre(@RequestParam Set<String> codesSemestre) {
        return ResponseEntity.ok(service.findAllByCodesSemestre(codesSemestre));
    }

    @GetMapping
    public ResponseEntity<ModulePagingResponse> getAll(@RequestParam(defaultValue = "0") @Min(0) int page,
                                                       @RequestParam(defaultValue = "10") @Range(min = 1, max = 100) int size,
                                                       @RequestParam(defaultValue = "false") boolean includeSemestre,
                                                       @RequestParam(defaultValue = "false") boolean includeElements) {

        return ResponseEntity.ok(service.findAll(page, size, includeSemestre, includeElements));
    }

    @PatchMapping("/{codeModule}")
    public ResponseEntity<ModuleResponse> update(@PathVariable String codeModule,
                                                 @Valid @RequestBody ModuleUpdateRequest request) {

        return ResponseEntity.ok(service.update(codeModule, request));
    }

    @DeleteMapping("/{codeModule}")
    public ResponseEntity<Void> deleteById(@PathVariable String codeModule) {
        service.deleteById(codeModule);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/bulk")
    public ResponseEntity<Void> deleteAllByIds(@RequestBody @NotEmpty Set<@NotBlank String> codesModule) {
        service.deleteAllByIds(codesModule);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/semestre/{codeSemestre}")
    public ResponseEntity<Void> deleteAllByCodeSemestre(@PathVariable String codeSemestre) {
        service.deleteAllByCodeSemestre(codeSemestre);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/semestre/bulk")
    public ResponseEntity<Void> deleteAllByCodesSemestre(@RequestBody @NotEmpty Set<@NotBlank String> codesSemestre) {
        service.deleteAllByCodesSemestre(codesSemestre);
        return ResponseEntity.noContent().build();
    }
}
