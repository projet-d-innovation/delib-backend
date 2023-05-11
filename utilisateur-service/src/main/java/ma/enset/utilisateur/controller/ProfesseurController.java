package ma.enset.utilisateur.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import ma.enset.utilisateur.constant.CoreConstants;
import ma.enset.utilisateur.dto.PagingResponse;
import ma.enset.utilisateur.dto.ProfesseurCreateRequestDTO;
import ma.enset.utilisateur.dto.ProfesseurResponseDTO;
import ma.enset.utilisateur.dto.ProfesseurUpdateRequestDTO;
import ma.enset.utilisateur.model.Utilisateur;
import ma.enset.utilisateur.service.UtilisateurService;
import ma.enset.utilisateur.util.ProfesseurMapper;
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
@RequestMapping("/api/v1/utilisateurs/professeurs")
@AllArgsConstructor
@Validated
public class ProfesseurController {

    private final ProfesseurMapper professeurMapper;
    private final UtilisateurService professeurService;

    private final String ROLE_ID = CoreConstants.RoleID.ROLE_PROFESSEUR;


    @GetMapping("{code}")
    public ResponseEntity<ProfesseurResponseDTO> findByCode(
            @PathVariable String code
    ) {
        Utilisateur professeur = professeurService.findByCodeUtilisateur(code, ROLE_ID);

        ProfesseurResponseDTO professeurResponse = professeurMapper.toProfesseurResponse(professeur);

        return ResponseEntity
                .ok()
                .body(professeurResponse);
    }

    @GetMapping("/bulk")
    public ResponseEntity<Iterable<ProfesseurResponseDTO>> findByCodes(
            @NotEmpty @RequestParam List<String> codes
    ) {
        List<Utilisateur> professeurs = professeurService.findAllByCodeUtilisateur(codes, ROLE_ID);
        List<ProfesseurResponseDTO> professeurResponses = professeurMapper.toProfesseurResponses(professeurs);

        return ResponseEntity
                .ok()
                .body(professeurResponses);
    }

    @PostMapping
    public ResponseEntity<ProfesseurResponseDTO> save(
            @Valid @RequestBody ProfesseurCreateRequestDTO professeurCreateRequest
    ) {
        Utilisateur professeur = professeurMapper.toUtilisateur(professeurCreateRequest);
        Utilisateur savedProfesseur = professeurService.save(professeur, ROLE_ID);

        ProfesseurResponseDTO professeurResponse = professeurMapper.toProfesseurResponse(savedProfesseur);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(professeurResponse);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<ProfesseurResponseDTO>> saveAll(
            @NotEmpty @RequestBody List<@Valid ProfesseurCreateRequestDTO> professeurCreateRequests
    ) {
        List<Utilisateur> professeurs = professeurMapper.createToUtilisateurs(professeurCreateRequests);
        List<Utilisateur> savedProfesseurs = professeurService.saveAll(professeurs, ROLE_ID);

        List<ProfesseurResponseDTO> professeurResponses = professeurMapper.toProfesseurResponses(savedProfesseurs);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(professeurResponses);
    }


    @GetMapping
    public ResponseEntity<PagingResponse<ProfesseurResponseDTO>> findAll(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Range(min = 1, max = 100) int size
    ) {

        Pageable pageRequest = PageRequest.of(page, size);
        Page<Utilisateur> elementsPage = professeurService.findAll(pageRequest, ROLE_ID);

        PagingResponse<ProfesseurResponseDTO> pagedResult = professeurMapper.toPagingResponse(elementsPage);


        return new ResponseEntity<>(
                pagedResult,
                HttpStatus.OK
        );
    }

    @PatchMapping("/{code}")
    public ResponseEntity<ProfesseurResponseDTO> update(
            @PathVariable String code,
            @Valid @RequestBody ProfesseurUpdateRequestDTO professeurUpdateRequest
    ) {
        Utilisateur professeur = professeurService.findByCodeUtilisateur(code, ROLE_ID);

        professeurMapper.updateRequestToProfesseur(professeurUpdateRequest, professeur);

        professeur.setCode(code);

        Utilisateur updatedProfesseur = professeurService.update(professeur, ROLE_ID);

        ProfesseurResponseDTO professeurResponse = professeurMapper.toProfesseurResponse(updatedProfesseur);

        return ResponseEntity
                .ok()
                .body(professeurResponse);
    }

    @PatchMapping("/bulk")
    public ResponseEntity<List<ProfesseurResponseDTO>> updateAll(
            @NotEmpty @RequestBody List<@Valid ProfesseurUpdateRequestDTO> professeurUpdateRequests
    ) {

        List<String> codes = professeurMapper.toProfesseurCodes(professeurUpdateRequests);

        List<Utilisateur> professeurs = professeurService.findAllByCodeUtilisateur(codes, ROLE_ID);

        professeurMapper.updateRequestsToProfesseurs(professeurUpdateRequests, professeurs);

        List<Utilisateur> updatedProfesseurs = professeurService.updateAll(professeurs, ROLE_ID);

        List<ProfesseurResponseDTO> professeurResponses = professeurMapper.toProfesseurResponses(updatedProfesseurs);

        return ResponseEntity
                .ok()
                .body(professeurResponses);
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<Void> delete(
            @PathVariable String code
    ) {
        professeurService.deleteByCodeUtilisateur(code, ROLE_ID);
        return ResponseEntity
                .noContent()
                .build();
    }

    @DeleteMapping("/bulk")
    public ResponseEntity<Void> deleteAll(
            @NotEmpty @RequestParam List<String> codes
    ) {
        professeurService.deleteAllByCodeUtilisateur(codes, ROLE_ID);
        return ResponseEntity
                .noContent()
                .build();
    }


}
