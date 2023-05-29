package ma.enset.utilisateur.controller;

import io.micrometer.common.lang.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import ma.enset.utilisateur.dto.IncludeParams;
import ma.enset.utilisateur.dto.PagingResponse;
import ma.enset.utilisateur.dto.utilisateur.UtilisateurCreateRequest;
import ma.enset.utilisateur.dto.utilisateur.UtilisateurResponse;
import ma.enset.utilisateur.dto.utilisateur.UtilisateurUpdateRequest;
import ma.enset.utilisateur.service.UtilisateurService;
import org.hibernate.validator.constraints.Range;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/utilisateurs")
@AllArgsConstructor
@Validated
public class UtilisateurController {

    private final UtilisateurService service;

    @GetMapping
    public ResponseEntity<PagingResponse<UtilisateurResponse>> findAll(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Range(min = 1, max = 100) int size,
            @RequestParam(defaultValue = "", required = false) @Nullable String search,
            @Valid IncludeParams includes
    ) {
        return ResponseEntity
                .ok()
                .body(service.findAll(page, size, search, includes));
    }

    @GetMapping("/role/{roleId}")
    public ResponseEntity<PagingResponse<UtilisateurResponse>> findAllByRole(
            @PathVariable("roleId") @NotBlank String roleId,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Range(min = 1, max = 100) int size,
            @RequestParam(defaultValue = "", required = false) @Nullable String search,
            @Valid IncludeParams includes
    ) {
        return ResponseEntity
                .ok()
                .body(service.findAllByRole(page, size, search, roleId, includes));
    }

    @GetMapping("/group/{roleGroup}")
    public ResponseEntity<PagingResponse<UtilisateurResponse>> findAllByGroup(
            @PathVariable("roleGroup") @NotBlank String roleGroup,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Range(min = 1, max = 100) int size,
            @RequestParam(defaultValue = "", required = false) @Nullable String search,
            @Valid IncludeParams includes
    ) {
        return ResponseEntity
                .ok()
                .body(service.findAllByGroup(page, size, search, roleGroup, includes));
    }

    @GetMapping("/{code}")
    public ResponseEntity<UtilisateurResponse> findById(
            @PathVariable("code") @NotBlank String code,
            @Valid IncludeParams includes
    ) {
        return ResponseEntity
                .ok()
                .body(service.findById(code, includes));
    }

    @GetMapping("/bulk")
    public ResponseEntity<List<UtilisateurResponse>> findAllById(
            @RequestParam @Valid Set<@NotBlank String> codes,
            @Valid IncludeParams includes
    ) {
        return ResponseEntity
                .ok()
                .body(service.findAllById(codes, includes));
    }

    @GetMapping("/exists")
    public ResponseEntity<Void> existsById(
            @RequestParam @NotEmpty Set<@NotBlank String> codes
    ) {
        service.exists(codes);
        return ResponseEntity
                .noContent()
                .build();
    }

    @PostMapping
    public ResponseEntity<UtilisateurResponse> save(
            @RequestBody @Valid UtilisateurCreateRequest request
    ) {
        return ResponseEntity
                .ok()
                .body(service.save(request));
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<UtilisateurResponse>> saveAll(
            @RequestBody @Valid List<UtilisateurCreateRequest> requests
    ) {
        return ResponseEntity
                .ok()
                .body(service.saveAll(requests));
    }

    @PatchMapping("/{code}")
    public ResponseEntity<UtilisateurResponse> update(
            @PathVariable("code") @NotBlank String code,
            @RequestBody @Valid UtilisateurUpdateRequest request
    ) {
        return ResponseEntity
                .ok()
                .body(service.update(code, request));
    }

    @PatchMapping("/bulk")
    public ResponseEntity<List<UtilisateurResponse>> updateAll(
            @RequestBody @Valid List<UtilisateurUpdateRequest> requests
    ) {
        return ResponseEntity
                .ok()
                .body(service.updateAll(requests));
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<Void> deleteById(
            @PathVariable("code") @NotBlank String code
    ) {
        service.deleteById(code);
        return ResponseEntity
                .noContent()
                .build();
    }

    @DeleteMapping("/bulk")
    public ResponseEntity<Void> deleteAllById(
            @RequestParam @Valid Set<@NotBlank String> codes
    ) {
        service.deleteAllById(codes);
        return ResponseEntity
                .noContent()
                .build();
    }

    @PatchMapping("/departement/bulk")
    public ResponseEntity<Void> handleKeyDepartementDeletion(
            @RequestParam @Valid Set<@NotBlank String> codesDepartement
    ) {
        service.handleKeyDepartementDeletion(codesDepartement);
        return ResponseEntity
                .noContent()
                .build();
    }
}
