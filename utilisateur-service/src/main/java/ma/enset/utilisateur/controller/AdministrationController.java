package ma.enset.utilisateur.controller;

import io.micrometer.common.lang.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import ma.enset.utilisateur.constant.CoreConstants;
import ma.enset.utilisateur.dto.UtilisateurCreateRequestDTO;
import ma.enset.utilisateur.dto.UtilisateurNestedRolesResponseDTO;
import ma.enset.utilisateur.dto.UtilisateurResponseDTO;
import ma.enset.utilisateur.dto.UtilisateurUpdateRequestDTO;
import ma.enset.utilisateur.model.Utilisateur;
import ma.enset.utilisateur.service.UtilisateurService;
import ma.enset.utilisateur.util.UtilisateurMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/utilisateurs/admin")
@AllArgsConstructor
@Validated
public class AdministrationController {

    private final UtilisateurMapper utilisateurMapper;
    private final UtilisateurService utilisateurService;


    @GetMapping("perms")
    public ResponseEntity<UtilisateurNestedRolesResponseDTO> findPermsByCode(
            @NotBlank @RequestParam String code
    ) {
        Utilisateur utilisateur = utilisateurService.findById(code);

        UtilisateurNestedRolesResponseDTO utilisateurResponse = utilisateurMapper.toUtilisateurWithRoleAndPermsResponse(utilisateur);

        return new ResponseEntity<>(
                utilisateurResponse,
                HttpStatus.OK
        );
    }

    @GetMapping("perms/many")
    public ResponseEntity<List<UtilisateurNestedRolesResponseDTO>> findManyPermsByCode(
            @RequestParam List<String> codes
    ) {

        List<Utilisateur> utilisateurs = utilisateurService.findManyById(codes);

        List<UtilisateurNestedRolesResponseDTO> utilisateurResponse = utilisateurMapper.toUtilisateurWithRoleAndPermsResponses(utilisateurs);

        return new ResponseEntity<>(
                utilisateurResponse,
                HttpStatus.OK
        );
    }


    @GetMapping("/code")
    public ResponseEntity<UtilisateurResponseDTO> findByCode(
            @RequestParam String code,
            @Nullable @RequestParam(defaultValue = "false")
            boolean includeRole
    ) {
        Utilisateur utilisateur = utilisateurService.findById(code);

        UtilisateurResponseDTO utilisateurResponse = null;

        if (includeRole)
            utilisateurResponse = utilisateurMapper.toUtilisateurWithRoleResponse(utilisateur);
        else
            utilisateurResponse = utilisateurMapper.toUtilisateurWithoutRoleResponse(utilisateur);

        return new ResponseEntity<>(
                utilisateurResponse,
                HttpStatus.OK
        );
    }


    @GetMapping("code/many")
    public ResponseEntity<Iterable<UtilisateurResponseDTO>> findByCodes(
            @RequestParam List<String> codes,
            @Nullable @RequestParam(defaultValue = "false")
            boolean includeRole
    ) {
        List<Utilisateur> utilisateurs = utilisateurService.findManyById(codes);
        List<UtilisateurResponseDTO> utilisateurResponses = null;

        if (includeRole)
            utilisateurResponses = utilisateurMapper.toUtilisateurWithRoleResponses(utilisateurs);
        else
            utilisateurResponses = utilisateurMapper.toUtilisateurWithoutRoleResponses(utilisateurs);

        return new ResponseEntity<>(
                utilisateurResponses,
                HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<UtilisateurResponseDTO> create(
            @Valid @RequestBody UtilisateurCreateRequestDTO utilisateurCreateRequest
    ) {
        Utilisateur utilisateur = utilisateurMapper.toUtilisateur(utilisateurCreateRequest);
        Utilisateur createdUtilisateur = utilisateurService.create(utilisateur);

        UtilisateurResponseDTO utilisateurResponse = utilisateurMapper.toUtilisateurWithRoleResponse(createdUtilisateur);

        return new ResponseEntity<>(
                utilisateurResponse,
                HttpStatus.CREATED
        );
    }

    @PostMapping("/many")
    public ResponseEntity<List<UtilisateurResponseDTO>> createMany(
            @Valid @RequestBody List<UtilisateurCreateRequestDTO> utilisateurCreateRequests
    ) {
        List<Utilisateur> utilisateurs = utilisateurMapper.createToUtilisateurs(utilisateurCreateRequests);

        List<Utilisateur> createdUtilisateurs = utilisateurService.createMany(utilisateurs);

        List<UtilisateurResponseDTO> utilisateurResponses = utilisateurMapper.toUtilisateurWithRoleResponses(createdUtilisateurs);

        return new ResponseEntity<>(
                utilisateurResponses,
                HttpStatus.CREATED
        );
    }


    @GetMapping
    public ResponseEntity<Page<UtilisateurResponseDTO>> findAll(
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = CoreConstants.ValidationMessage.PAGINATION_PAGE_MIN)
            int page,
            @RequestParam(defaultValue = "20")
            @Min(value = 1, message = CoreConstants.ValidationMessage.PAGINATION_SIZE_MIN)
            @Max(value = 20, message = CoreConstants.ValidationMessage.PAGINATION_SIZE_MAX)
            int size,
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

        return new ResponseEntity<>(
                pagedResult,
                HttpStatus.OK
        );
    }

    @GetMapping("{role}")
    public ResponseEntity<Page<UtilisateurResponseDTO>> findAllByRole(
            @NotBlank @PathVariable String role,
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = CoreConstants.ValidationMessage.PAGINATION_PAGE_MIN)
            int page,

            @RequestParam(defaultValue = "20")
            @Min(value = 1, message = CoreConstants.ValidationMessage.PAGINATION_SIZE_MIN)
            @Max(value = 20, message = CoreConstants.ValidationMessage.PAGINATION_SIZE_MAX)
            int size,
            @Nullable @RequestParam(defaultValue = "false")
            boolean includeRole
    ) {

        Pageable pageRequest = PageRequest.of(page, size);
        Page<Utilisateur> utilisateursPage = utilisateurService.findAll(pageRequest, role);

        Page<UtilisateurResponseDTO> pagedResult = null;

        if (includeRole)
            pagedResult = utilisateursPage.map(utilisateurMapper::toUtilisateurWithRoleResponse);
        else
            pagedResult = utilisateursPage.map(utilisateurMapper::toUtilisateurWithoutRoleResponse);

        return new ResponseEntity<>(
                pagedResult,
                HttpStatus.OK
        );
    }

    @PutMapping
    public ResponseEntity<UtilisateurResponseDTO> update(
            @Valid @RequestBody UtilisateurUpdateRequestDTO utilisateurUpdateRequest
    ) {
        Utilisateur utilisateur = utilisateurMapper.toUtilisateur(utilisateurUpdateRequest);
        Utilisateur updatedUtilisateur = utilisateurService.update(utilisateur);

        UtilisateurResponseDTO utilisateurResponse = utilisateurMapper.toUtilisateurWithRoleResponse(updatedUtilisateur);

        return new ResponseEntity<>(
                utilisateurResponse,
                HttpStatus.OK
        );
    }

    @PutMapping("many")
    public ResponseEntity<List<UtilisateurResponseDTO>> updateMany(
            @Valid @RequestBody List<UtilisateurUpdateRequestDTO> utilisateurUpdateRequests
    ) {
        List<Utilisateur> utilisateurs = utilisateurMapper.updateToUtilisateurs(utilisateurUpdateRequests);
        List<Utilisateur> updatedUtilisateurs = utilisateurService.updateMany(utilisateurs);

        List<UtilisateurResponseDTO> utilisateurResponses = utilisateurMapper.toUtilisateurWithRoleResponses(updatedUtilisateurs);

        return new ResponseEntity<>(
                utilisateurResponses,
                HttpStatus.OK
        );
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(
            @RequestParam String code
    ) {
        utilisateurService.deleteById(code);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("many")
    public ResponseEntity<Void> deleteMany(
            @RequestParam List<String> codes
    ) {
        utilisateurService.deleteManyById(codes);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
