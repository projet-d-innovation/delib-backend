package ma.enset.utilisateur.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import ma.enset.utilisateur.constant.CoreConstants;
import ma.enset.utilisateur.dto.ProfesseurCreateRequestDTO;
import ma.enset.utilisateur.dto.ProfesseurResponseDTO;
import ma.enset.utilisateur.dto.ProfesseurUpdateRequestDTO;
import ma.enset.utilisateur.model.Utilisateur;
import ma.enset.utilisateur.service.UtilisateurService;
import ma.enset.utilisateur.util.ProfesseurMapper;
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

    private final String ROLE_ID = "PROFESSEUR";


    @GetMapping("/code")
    public ResponseEntity<ProfesseurResponseDTO> findByCode(
            @RequestParam String code
    ) {
        Utilisateur professeur = professeurService.findById(code, ROLE_ID);

        ProfesseurResponseDTO professeurResponse = professeurMapper.toProfesseurResponse(professeur);

        return new ResponseEntity<>(
                professeurResponse,
                HttpStatus.OK
        );
    }

    @GetMapping("/code/many")
    public ResponseEntity<Iterable<ProfesseurResponseDTO>> findByCodes(
            @RequestParam List<String> codes
    ) {
        List<Utilisateur> professeurs = professeurService.findManyById(codes, ROLE_ID);
        List<ProfesseurResponseDTO> professeurResponses = professeurMapper.toProfesseurResponses(professeurs);

        return new ResponseEntity<>(
                professeurResponses,
                HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<ProfesseurResponseDTO> create(
            @Valid @RequestBody ProfesseurCreateRequestDTO professeurCreateRequest
    ) {
        Utilisateur professeur = professeurMapper.toUtilisateur(professeurCreateRequest);
        Utilisateur createdProfesseur = professeurService.create(professeur, ROLE_ID);

        ProfesseurResponseDTO professeurResponse = professeurMapper.toProfesseurResponse(createdProfesseur);

        return new ResponseEntity<>(
                professeurResponse,
                HttpStatus.CREATED
        );
    }

    @PostMapping("/many")
    public ResponseEntity<List<ProfesseurResponseDTO>> createMany(
            @Valid @RequestBody List<ProfesseurCreateRequestDTO> professeurCreateRequests
    ) {
        List<Utilisateur> professeurs = professeurMapper.createToUtilisateurs(professeurCreateRequests);
        List<Utilisateur> createdProfesseurs = professeurService.createMany(professeurs, ROLE_ID);

        List<ProfesseurResponseDTO> professeurResponses = professeurMapper.toProfesseurResponses(createdProfesseurs);

        return new ResponseEntity<>(
                professeurResponses,
                HttpStatus.CREATED
        );
    }


    @GetMapping
    public ResponseEntity<Page<ProfesseurResponseDTO>> findAll(
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = CoreConstants.ValidationMessage.PAGINATION_PAGE_MIN)
            int page,

            @RequestParam(defaultValue = "20")
            @Min(value = 1, message = CoreConstants.ValidationMessage.PAGINATION_SIZE_MIN)
            @Max(value = 20, message = CoreConstants.ValidationMessage.PAGINATION_SIZE_MAX)
            int size
    ) {

        Pageable pageRequest = PageRequest.of(page, size);
        Page<Utilisateur> elementsPage = professeurService.findAll(pageRequest, ROLE_ID);

        Page<ProfesseurResponseDTO> pagedResult = elementsPage.map(professeurMapper::toProfesseurResponse);


        return new ResponseEntity<>(
                pagedResult,
                HttpStatus.OK
        );
    }

    @PutMapping
    public ResponseEntity<ProfesseurResponseDTO> update(
            @Valid @RequestBody ProfesseurUpdateRequestDTO professeurUpdateRequest
    ) {
        Utilisateur professeur = professeurMapper.toUtilisateur(professeurUpdateRequest);
        Utilisateur updatedProfesseur = professeurService.update(professeur, ROLE_ID);

        ProfesseurResponseDTO professeurResponse = professeurMapper.toProfesseurResponse(updatedProfesseur);

        return new ResponseEntity<>(
                professeurResponse,
                HttpStatus.OK
        );
    }

    @PutMapping("/many")
    public ResponseEntity<List<ProfesseurResponseDTO>> updateMany(
            @Valid @RequestBody List<ProfesseurUpdateRequestDTO> professeurUpdateRequests
    ) {
        List<Utilisateur> professeurs = professeurMapper.updateToUtilisateurs(professeurUpdateRequests);
        List<Utilisateur> updatedProfesseurs = professeurService.updateMany(professeurs, ROLE_ID);

        List<ProfesseurResponseDTO> professeurResponses = professeurMapper.toProfesseurResponses(updatedProfesseurs);

        return new ResponseEntity<>(
                professeurResponses,
                HttpStatus.OK
        );
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(
            @RequestParam String code
    ) {
        professeurService.deleteById(code, ROLE_ID);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/many")
    public ResponseEntity<Void> deleteMany(
            @RequestParam List<String> codes
    ) {
        professeurService.deleteManyById(codes, ROLE_ID);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
