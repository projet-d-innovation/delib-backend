package ma.enset.element.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import ma.enset.element.dto.*;
import ma.enset.element.service.ElementService;
import org.hibernate.validator.constraints.Range;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/elements")
@RequiredArgsConstructor
@Validated
public class ElementController {
    private final ElementService service;

    @PostMapping
    public ResponseEntity<ElementResponse> save(@Valid @RequestBody ElementCreationRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.save(request));
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<ElementResponse>> saveAll(@RequestBody @NotEmpty List<@Valid ElementCreationRequest> request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.saveAll(request));
    }

    @GetMapping("/{codeElement}")
    public ResponseEntity<ElementResponse> get(@PathVariable String codeElement) {
        return ResponseEntity.ok(service.findById(codeElement));
    }

    @GetMapping("/bulk")
    public ResponseEntity<List<ElementResponse>> getAllByIds(@RequestParam @NotEmpty Set<@NotBlank String> codesElement) {
        return ResponseEntity.ok(service.findAllByIds(codesElement));
    }

    @GetMapping
    public ResponseEntity<ElementPagingResponse> getAll(@RequestParam(defaultValue = "0") @Min(0) int page,
                                                        @RequestParam(defaultValue = "10") @Range(min = 1, max = 100) int size) {

        return ResponseEntity.ok(service.findAll(page, size));
    }

    @GetMapping("/module/{codeModule}")
    public ResponseEntity<List<ElementResponse>> getModuleElements(@PathVariable String codeModule) {
        return ResponseEntity.ok(service.findModuleElements(codeModule));
    }

    @GetMapping("/module/bulk")
    public ResponseEntity<List<ModuleElementResponse>> getAllModulesElements(
                                        @RequestParam @NotEmpty Set<@NotBlank String> codesModule) {

        return ResponseEntity.ok(service.findAllModulesElements(codesModule));
    }

    @GetMapping("/professeur/{codeProfesseur}")
    public ResponseEntity<List<ElementResponse>> getProfesseurElements(@PathVariable String codeProfesseur) {
        return ResponseEntity.ok(service.findProfesseurElements(codeProfesseur));
    }

    @GetMapping("/professeur/bulk")
    public ResponseEntity<List<ProfesseurElementsResponse>> getAllProfesseursElements(
                                    @RequestParam @NotEmpty Set<@NotBlank String> codesProfesseur) {

        return ResponseEntity.ok(service.findAllProfesseursElements(codesProfesseur));
    }

    @GetMapping("/exist")
    public ResponseEntity<Void> existAll(@RequestParam @NotEmpty Set<@NotBlank String> codesElement) {
        service.existAllByIds(codesElement);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{codeElement}")
    public ResponseEntity<ElementResponse> update(@PathVariable String codeElement,
                                                  @Valid @RequestBody ElementUpdateRequest request) {

        return ResponseEntity.ok(service.update(codeElement, request));
    }

    @DeleteMapping("/{codeElement}")
    public ResponseEntity<Void> delete(@PathVariable String codeElement) {
        service.deleteById(codeElement);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/bulk")
    public ResponseEntity<Void> deleteAll(@RequestBody @NotEmpty Set<@NotBlank String> codesElement) {
        service.deleteAllByIds(codesElement);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/module/{codeModule}")
    public ResponseEntity<Void> deleteModuleElements(@PathVariable String codeModule) {
        service.deleteModuleElements(codeModule);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/module/bulk")
    public ResponseEntity<Void> deleteAllModulesElements(@RequestBody @NotEmpty Set<@NotBlank String> codesModule) {
        service.deleteAllModulesElements(codesModule);
        return ResponseEntity.noContent().build();
    }
}
