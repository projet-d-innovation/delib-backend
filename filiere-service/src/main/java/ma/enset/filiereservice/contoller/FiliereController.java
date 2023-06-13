package ma.enset.filiereservice.contoller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import ma.enset.filiereservice.dto.*;
import ma.enset.filiereservice.service.FiliereService;
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
@RequestMapping("/api/v1/filieres")
public class FiliereController {
    private final FiliereService filiereService;

    @PostMapping
    public ResponseEntity<FiliereResponse> save(@Valid @RequestBody FiliereCreationRequest filiereCreationRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(filiereService.save(filiereCreationRequest));
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<FiliereResponse>> saveAll(@RequestBody @NotEmpty List<@Valid FiliereCreationRequest> filiereCreationRequestList) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(filiereService.saveAll(filiereCreationRequestList));
    }

    @GetMapping("/{codeFiliere}")
    public ResponseEntity<FiliereResponse> getById(@PathVariable("codeFiliere") String codeFiliere,
                                                   @RequestParam(defaultValue = "false") boolean includeDepartement,
                                                   @RequestParam(defaultValue = "false") boolean includeSemestre,
                                                   @RequestParam(defaultValue = "false") boolean includeRegleDeCalcule,
                                                   @RequestParam(defaultValue = "false") boolean includeChefFiliere) {
        return ResponseEntity
                .ok()
                .body(filiereService.findById(codeFiliere, includeDepartement, includeSemestre, includeRegleDeCalcule, includeChefFiliere));
    }

    @GetMapping("/bulk")
    public ResponseEntity<List<FiliereResponse>> getAllById(@RequestParam @NotEmpty Set<@NotBlank String> codeFiliere,
                                                            @RequestParam(defaultValue = "false") boolean includeDepartement,
                                                            @RequestParam(defaultValue = "false") boolean includeSemestre,
                                                            @RequestParam(defaultValue = "false") boolean includeRegleDeCalcule,
                                                            @RequestParam(defaultValue = "false") boolean includeChefFiliere) {
        return ResponseEntity
                .ok()
                .body(filiereService.findAllById(codeFiliere, includeDepartement, includeSemestre, includeRegleDeCalcule, includeChefFiliere));
    }

    @GetMapping
    public ResponseEntity<FilierePagingResponse> getAll(@RequestParam(defaultValue = "0") @Min(0) int page,
                                                        @RequestParam(defaultValue = "10") @Range(min = 1, max = 100) int size,
                                                        @RequestParam(defaultValue = "") String searchByIntitute,
                                                        @RequestParam(defaultValue = "false") boolean includeDepartement,
                                                        @RequestParam(defaultValue = "false") boolean includeSemestre,
                                                        @RequestParam(defaultValue = "false") boolean includeRegleDeCalcule,
                                                        @RequestParam(defaultValue = "false") boolean includeChefFiliere

    ) {
        return ResponseEntity
                .ok()
                .body(filiereService.findAll(page, size, searchByIntitute, includeDepartement, includeSemestre, includeRegleDeCalcule, includeChefFiliere));
    }

    @PatchMapping("/{codeFiliere}")
    public ResponseEntity<FiliereResponse> update(@PathVariable String codeFiliere,
                                                  @Valid @RequestBody FiliereUpdateRequest filiereUpdateRequest) {
        return ResponseEntity
                .ok()
                .body(filiereService.update(codeFiliere, filiereUpdateRequest));
    }


    @DeleteMapping("/{codeFiliere}")
    public ResponseEntity<?> delete(@PathVariable String codeFiliere) {
        filiereService.deleteById(codeFiliere);
        return ResponseEntity
                .noContent()
                .build();
    }


    @DeleteMapping("/bulk")
    public ResponseEntity<?> deleteAll(@RequestParam @NotEmpty Set<@NotBlank String> codeFiliere) {
        filiereService.deleteById(codeFiliere);
        return ResponseEntity
                .noContent()
                .build();
    }


    @DeleteMapping("/departement/{codeDepartement}")
    public ResponseEntity<?> deleteByCodeDepartement(@PathVariable String codeDepartement) {
        filiereService.deleteByCodeDepartement(codeDepartement);
        return ResponseEntity
                .noContent()
                .build();
    }

    @DeleteMapping("/departement/bulk")
    public ResponseEntity<?> deleteAllByCodeDepartement(@RequestParam @NotEmpty Set<@NotBlank String> codeDepartement) {
        filiereService.deleteByCodeDepartement(codeDepartement);
        return ResponseEntity
                .noContent()
                .build();
    }

    @GetMapping("/departement/{codeDepartement}")
    public ResponseEntity<FiliereByDepartementResponse> getFilieresByCodeDepartement(
            @PathVariable String codeDepartement,
            @RequestParam(defaultValue = "false") boolean includeSemestre,
            @RequestParam(defaultValue = "false") boolean includeRegleDeCalcule,
            @RequestParam(defaultValue = "false") boolean includeChefFiliere
    ) {
        return ResponseEntity
                .ok()
                .body(filiereService.findByCodeDepartement(
                        codeDepartement,
                        includeSemestre,
                        includeRegleDeCalcule,
                        includeChefFiliere)
                );
    }

    @GetMapping("/departement/bulk")
    public ResponseEntity<List<FiliereByDepartementResponse>> getFilieresByCodeDepartement(
            @RequestParam Set<String> codeDepartements,
            @RequestParam(defaultValue = "false") boolean includeSemestre,
            @RequestParam(defaultValue = "false") boolean includeRegleDeCalcule,
            @RequestParam(defaultValue = "false") boolean includeChefFiliere
    ) {
        return ResponseEntity
                .ok()
                .body(filiereService.findAllByCodeDepartement(
                        codeDepartements,
                        includeSemestre,
                        includeRegleDeCalcule,
                        includeChefFiliere)
                );
    }

    @GetMapping("/exists")
    public ResponseEntity<Void> existsAll(@RequestParam @NotEmpty Set<@NotBlank String> codesFiliere) {
        filiereService.existsAllId(codesFiliere);
        return ResponseEntity.noContent().build();
    }


    @PatchMapping("/utilisateur")
    public ResponseEntity<Void> handleChefFiliereDeletion(
            @RequestParam @NotEmpty Set<@NotBlank String> codesUtilisateur
    ) {
        filiereService.handleUtilisateurDeletion(codesUtilisateur);
        return ResponseEntity
                .noContent()
                .build();
    }

}
