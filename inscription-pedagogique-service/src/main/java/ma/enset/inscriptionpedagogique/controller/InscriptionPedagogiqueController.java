package ma.enset.inscriptionpedagogique.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import ma.enset.inscriptionpedagogique.dto.InscriptionCreationRequest;
import ma.enset.inscriptionpedagogique.dto.InscriptionPagingResponse;
import ma.enset.inscriptionpedagogique.dto.InscriptionResponse;
import ma.enset.inscriptionpedagogique.dto.InscriptionUpdateRequest;
import ma.enset.inscriptionpedagogique.service.InscriptionPedagogiqueService;
import ma.enset.inscriptionpedagogique.validationgroups.OnBulkUpdate;
import org.hibernate.validator.constraints.Range;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/inscriptions-pedagogique")
@RequiredArgsConstructor
@Validated
public class InscriptionPedagogiqueController {

    private final InscriptionPedagogiqueService service;

    @PostMapping
    public ResponseEntity<InscriptionResponse> save(@Valid @RequestBody InscriptionCreationRequest request) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(service.save(request));
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<InscriptionResponse>> saveAll(@RequestBody @NotEmpty List<@Valid InscriptionCreationRequest> request) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(service.saveAll(request));
    }

    @GetMapping("/exists")
    public ResponseEntity<Void> existAllByIds(@RequestParam @NotEmpty Set<@NotBlank Long> ids) {
        service.existAllByIds(ids);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<InscriptionResponse> getById(@PathVariable long id,
                                                       @RequestParam(defaultValue = "false") boolean includeEtudiantInfo) {

        return ResponseEntity.ok(service.findById(id, includeEtudiantInfo));
    }

    @GetMapping("/bulk")
    public ResponseEntity<List<InscriptionResponse>> getAllByIds(@RequestParam Set<Long> ids,
                                                                 @RequestParam(defaultValue = "false") boolean includeEtudiantInfo) {

        return ResponseEntity.ok(service.findAllByIds(ids, includeEtudiantInfo));
    }

    @GetMapping
    public ResponseEntity<InscriptionPagingResponse> getAll(@RequestParam(defaultValue = "0") @Min(0) int page,
                                                            @RequestParam(defaultValue = "10") @Range(min = 1, max = 100) int size,
                                                            @RequestParam(defaultValue = "false") boolean includeEtudiantInfo) {

        return ResponseEntity.ok(service.findAll(page, size, includeEtudiantInfo));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<InscriptionResponse> update(@PathVariable long id,
                                                      @Valid @RequestBody InscriptionUpdateRequest request) {

        return ResponseEntity.ok(service.update(id, request));
    }

    @PatchMapping("/bulk")
    @Validated(OnBulkUpdate.class)
    public ResponseEntity<List<InscriptionResponse>> update(@RequestBody @NotEmpty List<@Valid InscriptionUpdateRequest> request) {
        return ResponseEntity.ok(service.updateAll(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/bulk")
    public ResponseEntity<Void> deleteAllByIds(@RequestBody @NotEmpty Set<@NotNull Long> ids) {
        service.deleteAllByIds(ids);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/etudiant/{codeEtudiant}")
    public ResponseEntity<Void> deleteAllByCodeEtudiant(@PathVariable String codeEtudiant) {
        service.deleteAllByCodeEtudiant(codeEtudiant);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/etudiant/bulk")
    public ResponseEntity<Void> deleteAllByCodesEtudiant(@RequestBody @NotEmpty Set<@NotBlank String> codesEtudiant) {
        service.deleteAllByCodesEtudiant(codesEtudiant);
        return ResponseEntity.noContent().build();
    }

}
