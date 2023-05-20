package ma.enset.utilisateur.controller;

import io.micrometer.common.lang.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import ma.enset.utilisateur.dto.IncludeParams;
import ma.enset.utilisateur.dto.PagingResponse;
import ma.enset.utilisateur.dto.utilisateur.UtilisateurCreateRequest;
import ma.enset.utilisateur.dto.utilisateur.UtilisateurResponse;
import ma.enset.utilisateur.dto.utilisateur.UtilisateurUpdateRequest;
import ma.enset.utilisateur.service.UtilisateurService;
import org.hibernate.validator.constraints.Range;
import org.springdoc.core.annotations.ParameterObject;
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

//    @PostMapping
//    public ResponseEntity<UtilisateurResponse> save(
//            @RequestBody UtilisateurCreateRequest request
//    ) {
//        return ResponseEntity
//                .ok()
//                .body(service.save(request));
//    }


//
//    @GetMapping("{code}/perms")
//    public ResponseEntity<UtilisateurNestedRolesResponseDTO> findPermsByCode(
//            @PathVariable String code
//    ) {
//        Utilisateur utilisateur = utilisateurService.findByCodeUtilisateur(code);
//
//        UtilisateurNestedRolesResponseDTO utilisateurResponse = utilisateurMapper.toUtilisateurWithRoleAndPermsResponse(utilisateur);
//
//        return ResponseEntity
//                .ok()
//                .body(utilisateurResponse);
//    }
//
//    @GetMapping("perms/bulk")
//    public ResponseEntity<List<UtilisateurNestedRolesResponseDTO>> findAllPermsByCode(
//            @NotEmpty @RequestParam List<String> codes
//    ) {
//
//        List<Utilisateur> utilisateurs = utilisateurService.findAllByCodeUtilisateur(codes);
//
//        List<UtilisateurNestedRolesResponseDTO> utilisateurResponse = utilisateurMapper.toUtilisateurWithRoleAndPermsResponses(utilisateurs);
//
//        return ResponseEntity
//                .ok()
//                .body(utilisateurResponse);
//    }
//
//
//    @GetMapping("{code}")
//    public ResponseEntity<UtilisateurResponseDTO> findByCode(
//            @PathVariable String code
//    ) {
//        Utilisateur utilisateur = utilisateurService.findByCodeUtilisateur(code);
//
//        UtilisateurResponseDTO utilisateurResponse = utilisateurMapper.toUtilisateurWithRoleResponse(utilisateur);
//
//        return ResponseEntity
//                .ok()
//                .body(utilisateurResponse);
//    }
//
//
//    @GetMapping("/bulk")
//    public ResponseEntity<Iterable<UtilisateurResponseDTO>> findByCodes(
//            @NotEmpty @RequestParam List<String> codes
//    ) {
//        List<Utilisateur> utilisateurs = utilisateurService.findAllByCodeUtilisateur(codes);
//        List<UtilisateurResponseDTO> utilisateurResponses = utilisateurMapper.toUtilisateurWithRoleResponses(utilisateurs);
//        return new ResponseEntity<>(
//                utilisateurResponses,
//                HttpStatus.OK
//        );
//    }
//
//    @PostMapping
//    public ResponseEntity<UtilisateurResponseDTO> save(
//            @Valid @RequestBody UtilisateurCreateRequestDTO utilisateurCreateRequest
//    ) {
//        Utilisateur utilisateur = utilisateurMapper.toUtilisateur(utilisateurCreateRequest);
//        Utilisateur savedUtilisateur = utilisateurService.save(utilisateur);
//
//        UtilisateurResponseDTO utilisateurResponse = utilisateurMapper.toUtilisateurWithRoleResponse(savedUtilisateur);
//
//        return ResponseEntity
//                .status(HttpStatus.CREATED)
//                .body(utilisateurResponse);
//    }
//
//    @PostMapping("/bulk")
//    public ResponseEntity<List<UtilisateurResponseDTO>> saveAll(
//            @NotEmpty @RequestBody List<@Valid UtilisateurCreateRequestDTO> utilisateurCreateRequests
//    ) {
//        List<Utilisateur> utilisateurs = utilisateurMapper.createToUtilisateurs(utilisateurCreateRequests);
//
//        List<Utilisateur> savedUtilisateurs = utilisateurService.saveAll(utilisateurs);
//
//        List<UtilisateurResponseDTO> utilisateurResponses = utilisateurMapper.toUtilisateurWithRoleResponses(savedUtilisateurs);
//
//        return new ResponseEntity<>(
//                utilisateurResponses,
//                HttpStatus.CREATED
//        );
//    }
//
//
//    @GetMapping
//    public ResponseEntity<PagingResponse<UtilisateurResponseDTO>> findAll(
//            @RequestParam(defaultValue = "0") @Min(0) int page,
//            @RequestParam(defaultValue = "10") @Range(min = 1, max = 100) int size,
//            @Nullable @RequestParam(defaultValue = "false")
//            boolean includeRole
//    ) {
//        Pageable pageRequest = PageRequest.of(page, size);
//        Page<Utilisateur> utilisateursPage = utilisateurService.findAll(pageRequest);
//
//        Page<UtilisateurResponseDTO> pagedResult = null;
//
//        if (includeRole)
//            pagedResult = utilisateursPage.map(utilisateurMapper::toUtilisateurWithRoleResponse);
//        else
//            pagedResult = utilisateursPage.map(utilisateurMapper::toUtilisateurWithoutRoleResponse);
//
//
//        PagingResponse<UtilisateurResponseDTO> response = utilisateurMapper.toPagingResponse(pagedResult);
//
//
//        return ResponseEntity
//                .ok()
//                .body(response);
//    }
//
//    @GetMapping("role/{role}")
//    public ResponseEntity<PagingResponse<UtilisateurResponseDTO>> findAllByRole(
//            @NotBlank @PathVariable String role,
//            @RequestParam(defaultValue = "0") @Min(0) int page,
//            @RequestParam(defaultValue = "10") @Range(min = 1, max = 100) int size
//    ) {
//
//        Pageable pageRequest = PageRequest.of(page, size);
//        Page<Utilisateur> utilisateursPage = utilisateurService.findAll(pageRequest, role);
//
//        Page<UtilisateurResponseDTO> pagedResult = utilisateursPage.map(utilisateurMapper::toUtilisateurWithRoleResponse);
//
//        PagingResponse<UtilisateurResponseDTO> response = utilisateurMapper.toPagingResponse(pagedResult);
//
//        return ResponseEntity
//                .ok()
//                .body(response);
//    }
//
//    @PatchMapping("{code}")
//    public ResponseEntity<UtilisateurResponseDTO> update(
//            @PathVariable String code,
//            @Valid @RequestBody UtilisateurUpdateRequestDTO utilisateurUpdateRequest
//    ) {
//
//        Utilisateur utilisateur = utilisateurService.findByCodeUtilisateur(code);
//
//        utilisateurMapper.updateRequestToUtilisateur(utilisateurUpdateRequest, utilisateur);
//
//        utilisateur.setCode(code);
//
//        Utilisateur updatedUtilisateur = utilisateurService.update(utilisateur);
//
//        UtilisateurResponseDTO utilisateurResponse = utilisateurMapper.toUtilisateurWithRoleResponse(updatedUtilisateur);
//
//        return ResponseEntity
//                .ok()
//                .body(utilisateurResponse);
//    }
//
//    @PatchMapping("bulk")
//    public ResponseEntity<List<UtilisateurResponseDTO>> updateAll(
//            @NotEmpty @RequestBody List<@Valid UtilisateurUpdateRequestDTO> utilisateurUpdateRequests
//    ) {
//
//        List<String> codes = utilisateurMapper.toUtilisateurCodes(utilisateurUpdateRequests);
//
//
//        List<Utilisateur> utilisateurs = utilisateurService.findAllByCodeUtilisateur(codes);
//
//        utilisateurMapper.updateRequestsToUtilisateurs(utilisateurUpdateRequests, utilisateurs);
//
//        List<Utilisateur> updatedUtilisateurs = utilisateurService.updateAll(utilisateurs);
//
//        List<UtilisateurResponseDTO> utilisateurResponses = utilisateurMapper.toUtilisateurWithRoleResponses(updatedUtilisateurs);
//
//        return new ResponseEntity<>(
//                utilisateurResponses,
//                HttpStatus.OK
//        );
//    }
//
//    @DeleteMapping("{code}")
//    public ResponseEntity<Void> delete(
//            @PathVariable String code
//    ) {
//        utilisateurService.deleteByCodeUtilisateur(code);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
//
//    @DeleteMapping("bulk")
//    public ResponseEntity<Void> deleteAll(
//            @NotEmpty @RequestParam List<String> codes
//    ) {
//        utilisateurService.deleteAllByCodeUtilisateur(codes);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
//

}
