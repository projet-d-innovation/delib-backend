package ma.enset.utilisateur.controller;

import io.micrometer.common.lang.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import ma.enset.utilisateur.dto.*;
import ma.enset.utilisateur.model.Utilisateur;
import ma.enset.utilisateur.service.UtilisateurService;
import ma.enset.utilisateur.util.UtilisateurMapper;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/administration/utilisateurs")
@AllArgsConstructor
@Validated
public class AdministrationController {

    private final UtilisateurMapper utilisateurMapper;
    private final UtilisateurService utilisateurService;


    @GetMapping("{code}/perms")
    public ResponseEntity<UtilisateurNestedRolesResponseDTO> findPermsByCode(
            @PathVariable String code
    ) {
        Utilisateur utilisateur = utilisateurService.findByCodeUtilisateur(code);

        UtilisateurNestedRolesResponseDTO utilisateurResponse = utilisateurMapper.toUtilisateurWithRoleAndPermsResponse(utilisateur);

        return ResponseEntity
                .ok()
                .body(utilisateurResponse);
    }

    @GetMapping("perms/bulk")
    public ResponseEntity<List<UtilisateurNestedRolesResponseDTO>> findAllPermsByCode(
            @NotEmpty @RequestParam List<String> codes
    ) {

        List<Utilisateur> utilisateurs = utilisateurService.findAllByCodeUtilisateur(codes);

        List<UtilisateurNestedRolesResponseDTO> utilisateurResponse = utilisateurMapper.toUtilisateurWithRoleAndPermsResponses(utilisateurs);

        return ResponseEntity
                .ok()
                .body(utilisateurResponse);
    }


    @GetMapping("{code}")
    public ResponseEntity<UtilisateurResponseDTO> findByCode(
            @PathVariable String code
    ) {
        Utilisateur utilisateur = utilisateurService.findByCodeUtilisateur(code);

        UtilisateurResponseDTO utilisateurResponse = utilisateurMapper.toUtilisateurWithRoleResponse(utilisateur);

        return ResponseEntity
                .ok()
                .body(utilisateurResponse);
    }


    @GetMapping("/bulk")
    public ResponseEntity<Iterable<UtilisateurResponseDTO>> findByCodes(
            @NotEmpty @RequestParam List<String> codes
    ) {
        List<Utilisateur> utilisateurs = utilisateurService.findAllByCodeUtilisateur(codes);
        List<UtilisateurResponseDTO> utilisateurResponses = utilisateurMapper.toUtilisateurWithRoleResponses(utilisateurs);
        return new ResponseEntity<>(
                utilisateurResponses,
                HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<UtilisateurResponseDTO> save(
            @Valid @RequestBody UtilisateurCreateRequestDTO utilisateurCreateRequest
    ) {
        Utilisateur utilisateur = utilisateurMapper.toUtilisateur(utilisateurCreateRequest);
        Utilisateur savedUtilisateur = utilisateurService.save(utilisateur);

        UtilisateurResponseDTO utilisateurResponse = utilisateurMapper.toUtilisateurWithRoleResponse(savedUtilisateur);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(utilisateurResponse);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<UtilisateurResponseDTO>> saveAll(
            @NotEmpty @RequestBody List<@Valid UtilisateurCreateRequestDTO> utilisateurCreateRequests
    ) {
        List<Utilisateur> utilisateurs = utilisateurMapper.createToUtilisateurs(utilisateurCreateRequests);

        List<Utilisateur> savedUtilisateurs = utilisateurService.saveAll(utilisateurs);

        List<UtilisateurResponseDTO> utilisateurResponses = utilisateurMapper.toUtilisateurWithRoleResponses(savedUtilisateurs);

        return new ResponseEntity<>(
                utilisateurResponses,
                HttpStatus.CREATED
        );
    }


    @GetMapping
    public ResponseEntity<PagingResponse<UtilisateurResponseDTO>> findAll(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Range(min = 1, max = 10) int size,
            @Nullable @RequestParam(defaultValue = "false")
            boolean includeRole
    ) {
        Pageable pageRequest = PageRequest.of(page, size);
        Page<Utilisateur> utilisateursPage = utilisateurService.findAll(pageRequest);

        Page<UtilisateurResponseDTO> pagedResult = null;

        if (includeRole)
            pagedResult = utilisateursPage.map(utilisateurMapper::toUtilisateurWithRoleResponse);
        else
            pagedResult = utilisateursPage.map(utilisateurMapper::toUtilisateurWithoutRoleResponse);


        PagingResponse<UtilisateurResponseDTO> response = utilisateurMapper.toPagingResponse(pagedResult);


        return ResponseEntity
                .ok()
                .body(response);
    }

    @GetMapping("role/{role}")
    public ResponseEntity<PagingResponse<UtilisateurResponseDTO>> findAllByRole(
            @NotBlank @PathVariable String role,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Range(min = 1, max = 10) int size
    ) {

        Pageable pageRequest = PageRequest.of(page, size);
        Page<Utilisateur> utilisateursPage = utilisateurService.findAll(pageRequest, role);

        Page<UtilisateurResponseDTO> pagedResult = utilisateursPage.map(utilisateurMapper::toUtilisateurWithRoleResponse);

        PagingResponse<UtilisateurResponseDTO> response = utilisateurMapper.toPagingResponse(pagedResult);

        return ResponseEntity
                .ok()
                .body(response);
    }

    @PatchMapping("{code}")
    public ResponseEntity<UtilisateurResponseDTO> update(
            @PathVariable String code,
            @Valid @RequestBody UtilisateurUpdateRequestDTO utilisateurUpdateRequest
    ) {

        Utilisateur utilisateur = utilisateurService.findByCodeUtilisateur(code);

        utilisateurMapper.updateRequestToUtilisateur(utilisateurUpdateRequest, utilisateur);

        utilisateur.setCode(code);

        Utilisateur updatedUtilisateur = utilisateurService.update(utilisateur);

        UtilisateurResponseDTO utilisateurResponse = utilisateurMapper.toUtilisateurWithRoleResponse(updatedUtilisateur);

        return ResponseEntity
                .ok()
                .body(utilisateurResponse);
    }

    @PatchMapping("bulk")
    public ResponseEntity<List<UtilisateurResponseDTO>> updateAll(
            @NotEmpty @RequestBody List<@Valid UtilisateurUpdateRequestDTO> utilisateurUpdateRequests
    ) {

        List<String> codes = utilisateurMapper.toUtilisateurCodes(utilisateurUpdateRequests);


        List<Utilisateur> utilisateurs = utilisateurService.findAllByCodeUtilisateur(codes);

        utilisateurMapper.updateRequestsToUtilisateurs(utilisateurUpdateRequests, utilisateurs);

        List<Utilisateur> updatedUtilisateurs = utilisateurService.updateAll(utilisateurs);

        List<UtilisateurResponseDTO> utilisateurResponses = utilisateurMapper.toUtilisateurWithRoleResponses(updatedUtilisateurs);

        return new ResponseEntity<>(
                utilisateurResponses,
                HttpStatus.OK
        );
    }

    @DeleteMapping("{code}")
    public ResponseEntity<Void> delete(
            @PathVariable String code
    ) {
        utilisateurService.deleteByCodeUtilisateur(code);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("bulk")
    public ResponseEntity<Void> deleteAll(
            @NotEmpty @RequestParam List<String> codes
    ) {
        utilisateurService.deleteAllByCodeUtilisateur(codes);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
