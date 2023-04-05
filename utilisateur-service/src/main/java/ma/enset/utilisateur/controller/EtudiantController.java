package ma.enset.utilisateur.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import ma.enset.utilisateur.constant.CoreConstants;
import ma.enset.utilisateur.dto.EtudiantCreateRequestDTO;
import ma.enset.utilisateur.dto.EtudiantResponseDTO;
import ma.enset.utilisateur.dto.EtudiantUpdateRequestDTO;
import ma.enset.utilisateur.model.Utilisateur;
import ma.enset.utilisateur.service.UtilisateurService;
import ma.enset.utilisateur.util.EtudiantMapper;
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

    private final String ROLE_ID = "ETUDIANT";


    @GetMapping("/code")
    public ResponseEntity<EtudiantResponseDTO> findByCode(
            @RequestParam String code
    ) {
        Utilisateur etudiant = etudiantService.findById(code, ROLE_ID);

        EtudiantResponseDTO etudiantResponse = etudiantMapper.toEtudiantResponse(etudiant);

        return new ResponseEntity<>(
                etudiantResponse,
                HttpStatus.OK
        );
    }
//
//    @GetMapping("/perms")
//    public ResponseEntity<EtudiantNestedRolesResponseDTO> findPermsByCode(
//            @NotBlank @RequestParam String code
//    ) {
//        Utilisateur etudiant = etudiantService.findById(code, ROLE_ID);
//
//        EtudiantNestedRolesResponseDTO etudiantResponse = etudiantMapper.toEtudiantWithRoleAndPermsResponse(etudiant);
//
//        return new ResponseEntity<>(
//                etudiantResponse,
//                HttpStatus.OK
//        );
//    }
//
//    @GetMapping("/perms/many")
//    public ResponseEntity<List<EtudiantNestedRolesResponseDTO>> findManyPermsByCode(
//            @RequestParam List<String> codes
//    ) {
//
//        List<Utilisateur> etudiants = etudiantService.findManyById(codes, ROLE_ID);
//
//        List<EtudiantNestedRolesResponseDTO> etudiantResponse = etudiantMapper.toEtudiantWithRoleAndPermsResponses(etudiants);
//
//        return new ResponseEntity<>(
//                etudiantResponse,
//                HttpStatus.OK
//        );
//    }


    @GetMapping("/code/many")
    public ResponseEntity<Iterable<EtudiantResponseDTO>> findByCodes(
            @RequestParam List<String> codes
    ) {
        List<Utilisateur> etudiants = etudiantService.findManyById(codes, ROLE_ID);
        List<EtudiantResponseDTO> etudiantResponses = etudiantMapper.toEtudiantResponses(etudiants);

        return new ResponseEntity<>(
                etudiantResponses,
                HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<EtudiantResponseDTO> create(
            @Valid @RequestBody EtudiantCreateRequestDTO etudiantCreateRequest
    ) {
        Utilisateur etudiant = etudiantMapper.toUtilisateur(etudiantCreateRequest);
        Utilisateur createdEtudiant = etudiantService.create(etudiant, ROLE_ID);

        EtudiantResponseDTO etudiantResponse = etudiantMapper.toEtudiantResponse(createdEtudiant);

        return new ResponseEntity<>(
                etudiantResponse,
                HttpStatus.CREATED
        );
    }

    @PostMapping("/many")
    public ResponseEntity<List<EtudiantResponseDTO>> createMany(
            @Valid @RequestBody List<EtudiantCreateRequestDTO> etudiantCreateRequests
    ) {
        List<Utilisateur> etudiants = etudiantMapper.createToUtilisateurs(etudiantCreateRequests);
        List<Utilisateur> createdEtudiants = etudiantService.createMany(etudiants, ROLE_ID);

        List<EtudiantResponseDTO> etudiantResponses = etudiantMapper.toEtudiantResponses(createdEtudiants);

        return new ResponseEntity<>(
                etudiantResponses,
                HttpStatus.CREATED
        );
    }


    @GetMapping
    public ResponseEntity<Page<EtudiantResponseDTO>> findAll(
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = CoreConstants.ValidationMessage.PAGINATION_PAGE_MIN)
            int page,

            @RequestParam(defaultValue = "20")
            @Min(value = 1, message = CoreConstants.ValidationMessage.PAGINATION_SIZE_MIN)
            @Max(value = 20, message = CoreConstants.ValidationMessage.PAGINATION_SIZE_MAX)
            int size
    ) {

        Pageable pageRequest = PageRequest.of(page, size);
        Page<Utilisateur> elementsPage = etudiantService.findAll(pageRequest, ROLE_ID);

        Page<EtudiantResponseDTO> pagedResult = elementsPage.map(etudiantMapper::toEtudiantResponse);


        return new ResponseEntity<>(
                pagedResult,
                HttpStatus.OK
        );
    }

    @PutMapping
    public ResponseEntity<EtudiantResponseDTO> update(
            @Valid @RequestBody EtudiantUpdateRequestDTO etudiantUpdateRequest
    ) {
        Utilisateur etudiant = etudiantMapper.toUtilisateur(etudiantUpdateRequest);
        Utilisateur updatedEtudiant = etudiantService.update(etudiant, ROLE_ID);

        EtudiantResponseDTO etudiantResponse = etudiantMapper.toEtudiantResponse(updatedEtudiant);

        return new ResponseEntity<>(
                etudiantResponse,
                HttpStatus.OK
        );
    }

    @PutMapping("/many")
    public ResponseEntity<List<EtudiantResponseDTO>> updateMany(
            @Valid @RequestBody List<EtudiantUpdateRequestDTO> etudiantUpdateRequests
    ) {
        List<Utilisateur> etudiants = etudiantMapper.updateToUtilisateurs(etudiantUpdateRequests);
        List<Utilisateur> updatedEtudiants = etudiantService.updateMany(etudiants, ROLE_ID);

        List<EtudiantResponseDTO> etudiantResponses = etudiantMapper.toEtudiantResponses(updatedEtudiants);

        return new ResponseEntity<>(
                etudiantResponses,
                HttpStatus.OK
        );
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(
            @RequestParam String code
    ) {
        etudiantService.deleteById(code, ROLE_ID);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/many")
    public ResponseEntity<Void> deleteMany(
            @RequestParam List<String> codes
    ) {
        etudiantService.deleteManyById(codes, ROLE_ID);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
