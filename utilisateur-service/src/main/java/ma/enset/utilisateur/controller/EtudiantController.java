package ma.enset.utilisateur.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import ma.enset.utilisateur.constant.CoreConstants;
import ma.enset.utilisateur.dto.EtudiantCreateRequestDTO;
import ma.enset.utilisateur.dto.EtudiantResponseDTO;
import ma.enset.utilisateur.dto.EtudiantUpdateRequestDTO;
import ma.enset.utilisateur.dto.PagingResponse;
import ma.enset.utilisateur.model.Utilisateur;
import ma.enset.utilisateur.service.UtilisateurService;
import ma.enset.utilisateur.util.EtudiantMapper;
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
@RequestMapping("/api/v1/utilisateurs/etudiants")
@AllArgsConstructor
@Validated
public class EtudiantController {

    private final EtudiantMapper etudiantMapper;
    private final UtilisateurService etudiantService;

    private final String ROLE_ID = CoreConstants.RoleID.ROLE_ETUDIANT;


    @GetMapping("{code}")
    public ResponseEntity<EtudiantResponseDTO> findByCode(
            @PathVariable String code
    ) {
        Utilisateur etudiant = etudiantService.findByCodeUtilisateur(code, ROLE_ID);

        EtudiantResponseDTO etudiantResponse = etudiantMapper.toEtudiantResponse(etudiant);

        return ResponseEntity
                .ok()
                .body(etudiantResponse);
    }


    @GetMapping("/bulk")
    public ResponseEntity<Iterable<EtudiantResponseDTO>> findByCodes(
            @NotEmpty @RequestParam List<String> codes
    ) {
        List<Utilisateur> etudiants = etudiantService.findAllByCodeUtilisateur(codes, ROLE_ID);
        List<EtudiantResponseDTO> etudiantResponses = etudiantMapper.toEtudiantResponses(etudiants);

        return ResponseEntity
                .ok()
                .body(etudiantResponses);
    }

    @PostMapping
    public ResponseEntity<EtudiantResponseDTO> save(
            @Valid @RequestBody EtudiantCreateRequestDTO etudiantCreateRequest
    ) {
        Utilisateur etudiant = etudiantMapper.toUtilisateur(etudiantCreateRequest);
        Utilisateur savedEtudiant = etudiantService.save(etudiant, ROLE_ID);

        EtudiantResponseDTO etudiantResponse = etudiantMapper.toEtudiantResponse(savedEtudiant);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(etudiantResponse);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<EtudiantResponseDTO>> saveAll(
            @NotEmpty @RequestBody List<@Valid EtudiantCreateRequestDTO> etudiantCreateRequests
    ) {
        List<Utilisateur> etudiants = etudiantMapper.createToUtilisateurs(etudiantCreateRequests);
        List<Utilisateur> savedEtudiants = etudiantService.saveAll(etudiants, ROLE_ID);

        List<EtudiantResponseDTO> etudiantResponses = etudiantMapper.toEtudiantResponses(savedEtudiants);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(etudiantResponses);
    }


    @GetMapping
    public ResponseEntity<PagingResponse<EtudiantResponseDTO>> findAll(
            @RequestParam(defaultValue ="") String search,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Range(min = 1, max = 100) int size
    ) {

        Pageable pageRequest = PageRequest.of(page, size);
        Page<Utilisateur> elementsPage = etudiantService.findAll(search,pageRequest, ROLE_ID);

        PagingResponse<EtudiantResponseDTO> pagedResult = etudiantMapper.toPagingResponse(elementsPage);

        return ResponseEntity
                .ok()
                .body(pagedResult);
    }

    @PatchMapping({"{code}"})
    public ResponseEntity<EtudiantResponseDTO> update(
            @PathVariable String code,
            @Valid @RequestBody EtudiantUpdateRequestDTO etudiantUpdateRequest
    ) {
        Utilisateur etudiant = etudiantService.findByCodeUtilisateur(code, ROLE_ID);

        etudiantMapper.updateRequestToEtudiant(etudiantUpdateRequest, etudiant);

        etudiant.setCode(code);

        Utilisateur updatedEtudiant = etudiantService.update(etudiant, ROLE_ID);

        EtudiantResponseDTO etudiantResponse = etudiantMapper.toEtudiantResponse(updatedEtudiant);

        return ResponseEntity
                .ok()
                .body(etudiantResponse);
    }

    @PatchMapping("/bulk")
    public ResponseEntity<List<EtudiantResponseDTO>> updateAll(
            @NotEmpty @RequestBody List<@Valid EtudiantUpdateRequestDTO> etudiantUpdateRequests
    ) {

        List<String> codes = etudiantMapper.toEtudiantCodes(etudiantUpdateRequests);

        List<Utilisateur> etudiants = etudiantService.findAllByCodeUtilisateur(codes, ROLE_ID);

        etudiantMapper.updateRequestsToEtudiants(etudiantUpdateRequests, etudiants);

        List<Utilisateur> updatedEtudiants = etudiantService.updateAll(etudiants, ROLE_ID);

        List<EtudiantResponseDTO> etudiantResponses = etudiantMapper.toEtudiantResponses(updatedEtudiants);

        return ResponseEntity
                .ok()
                .body(etudiantResponses);
    }

    @DeleteMapping({"{code}"})
    public ResponseEntity<Void> delete(
            @PathVariable String code
    ) {
        etudiantService.deleteByCodeUtilisateur(code, ROLE_ID);
        return ResponseEntity
                .noContent()
                .build();
    }

    @DeleteMapping("/bulk")
    public ResponseEntity<Void> deleteAll(
            @NotEmpty @RequestParam List<String> codes
    ) {
        etudiantService.deleteAllByCodeUtilisateur(codes, ROLE_ID);
        return ResponseEntity
                .noContent()
                .build();
    }


}
