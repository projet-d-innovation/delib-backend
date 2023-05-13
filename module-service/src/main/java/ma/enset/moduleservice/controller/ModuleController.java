package ma.enset.moduleservice.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import ma.enset.moduleservice.dto.ModuleCreationRequest;
import ma.enset.moduleservice.dto.ModulePagingResponse;
import ma.enset.moduleservice.dto.ModuleResponse;
import ma.enset.moduleservice.dto.ModuleUpdateRequest;
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

    @GetMapping("/{codeModule}")
    public ResponseEntity<ModuleResponse> get(@PathVariable String codeModule,
                                              @RequestParam(defaultValue = "false") boolean includeElements) {

        return ResponseEntity.ok(service.findById(codeModule, includeElements));
    }

    @GetMapping
    public ResponseEntity<ModulePagingResponse> getAll(@RequestParam(defaultValue = "0") @Min(0) int page,
                                                       @RequestParam(defaultValue = "10") @Range(min = 1, max = 100) int size,
                                                       @RequestParam(defaultValue = "false") boolean includeElements) {

        return ResponseEntity.ok(service.findAll(page, size, includeElements));
    }

    @GetMapping("/exists")
    public ResponseEntity<Void> existsAll(@RequestParam @NotEmpty Set<@NotBlank String> codesModule) {
        service.existsAllId(codesModule);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{codeModule}")
    public ResponseEntity<ModuleResponse> update(@PathVariable String codeModule,
                                                 @Valid @RequestBody ModuleUpdateRequest request) {

        return ResponseEntity.ok(service.update(codeModule, request));
    }

    @DeleteMapping("/{codeModule}")
    public ResponseEntity<Void> delete(@PathVariable String codeModule) {
        service.deleteById(codeModule);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteByCodeSemestre(@RequestParam String codeSemestre) {
        service.deleteByCodeSemestre(codeSemestre);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/bulk")
    public ResponseEntity<Void> deleteAll(@RequestBody @NotEmpty Set<@NotBlank String> codesModule) {
        service.deleteAllById(codesModule);
        return ResponseEntity.noContent().build();
    }
}
