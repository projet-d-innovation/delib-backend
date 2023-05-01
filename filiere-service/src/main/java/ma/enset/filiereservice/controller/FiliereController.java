package ma.enset.filiereservice.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import ma.enset.filiereservice.dto.FiliereCreationRequest;
import ma.enset.filiereservice.dto.FilierePagingResponse;
import ma.enset.filiereservice.dto.FiliereResponse;
import ma.enset.filiereservice.dto.FiliereUpdateRequest;
import ma.enset.filiereservice.model.Filiere;
import ma.enset.filiereservice.service.FiliereService;
import ma.enset.filiereservice.service.RegleDeCalculService;
import ma.enset.filiereservice.util.FiliereMapper;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Validated
@AllArgsConstructor
@RequestMapping("/api/v1/filieres")
public class FiliereController {


    private final FiliereService filiereService;
    private final FiliereMapper filiereMapper;

    private RegleDeCalculService regleDeCalculService;

    @PostMapping
    public ResponseEntity<FiliereResponse> save(@Valid @RequestBody FiliereCreationRequest filiereCreationRequest) {
        Filiere filiere = filiereMapper.toFiliere(filiereCreationRequest, regleDeCalculService);
        FiliereResponse filiereResponse = filiereMapper.toFiliereResponse(filiereService.save(filiere));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(filiereResponse);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<FiliereResponse>> saveAll(@RequestBody List<@Valid FiliereCreationRequest> filiereCreationRequestList) {
        List<Filiere> filiereList = filiereMapper.toFiliereList(filiereCreationRequestList, regleDeCalculService);
        List<FiliereResponse> filiereResponseList = filiereMapper.toFiliereResponseList(filiereService.saveAll(filiereList));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(filiereResponseList);
    }

    @GetMapping("/{codeFiliere}")
    public ResponseEntity<FiliereResponse> getByCodeFiliere(@PathVariable("codeFiliere") String codeFiliere) {

        Filiere foundFiliere = filiereService.findByCodeFiliere(codeFiliere);
        FiliereResponse foundFiliereDeResponse = filiereMapper.toFiliereResponse(foundFiliere);

        return ResponseEntity
                .ok()
                .body(foundFiliereDeResponse);
    }

    @GetMapping("/byCodeDepartement/{codeDepartement}")
    public ResponseEntity<List<FiliereResponse>> getByCodeDepartement(@PathVariable("codeDepartement") String codeDepartement) {

        List<Filiere> foundFilieres = filiereService.findByCodeDepartement(codeDepartement);
        List<FiliereResponse> foundFiliereResponseList = filiereMapper.toFiliereResponseList(foundFilieres);

        return ResponseEntity
                .ok()
                .body(foundFiliereResponseList);
    }


    @GetMapping("/isChef/{codeUser}")
    public ResponseEntity<Boolean> isThisUserAChef(@PathVariable("codeUser") String codeUser) {

        boolean isThisUserAChef = filiereService.isThisUserAChef(codeUser);

        return ResponseEntity
                .ok()
                .body(isThisUserAChef);
    }

    @GetMapping("/byCodeDeRegle/{codeRegle}")
    public ResponseEntity<List<FiliereResponse>> getByCodeRegle(@PathVariable("codeRegle") String codeRegle) {

        List<Filiere> foundFilieres = filiereService.findByCodeRegle(codeRegle);
        List<FiliereResponse> foundFiliereResponseList = filiereMapper.toFiliereResponseList(foundFilieres);

        return ResponseEntity
                .ok()
                .body(foundFiliereResponseList);
    }


    @GetMapping
    public ResponseEntity<FilierePagingResponse> getAll(@RequestParam(defaultValue = "0") @Min(0) int page,
                                                        @RequestParam(defaultValue = "10") @Range(min = 1, max = 10) int size) {

        Pageable pageRequest = PageRequest.of(page, size);
        Page<Filiere> filierePage = filiereService.findAll(pageRequest);
        FilierePagingResponse pagedResponse = filiereMapper.toPagingResponse(filierePage);

        return ResponseEntity
                .ok()
                .body(pagedResponse);
    }


    @DeleteMapping("/{codeFiliere}")
    public ResponseEntity<?> delete(@PathVariable("codeFiliere") String codeFiliere) {
        filiereService.deleteByCodeFiliere(codeFiliere);

        return ResponseEntity
                .noContent()
                .build();
    }

    @DeleteMapping("/bulk")
    public ResponseEntity<?> deleteAll(@RequestBody List<String> codeFiliere) {
        filiereService.deleteAllByCodeFiliere(codeFiliere);

        return ResponseEntity
                .noContent()
                .build();
    }


    @PutMapping("/{codeFiliere}/semestres/{codeSemestre}")
    public ResponseEntity<FiliereResponse> addSemestreToFiliere(@PathVariable("codeFiliere") String codeFiliere,
                                                                @PathVariable("codeSemestre") String codeSemestre) {

        Filiere filiere = filiereService.addSemestreToFiliere(codeFiliere, codeSemestre);
        FiliereResponse filiereResponse = filiereMapper.toFiliereResponse(filiere);

        return ResponseEntity
                .ok()
                .body(filiereResponse);
    }

    @DeleteMapping("/{codeFiliere}/semestres/{codeSemestre}")
    public ResponseEntity<?> removeSemestreFromFiliere(@PathVariable("codeFiliere") String codeFiliere,
                                                       @PathVariable("codeSemestre") String codeSemestre) {

        filiereService.deleteSemestreFromFiliere(codeFiliere, codeSemestre);

        return ResponseEntity
                .noContent()
                .build();
    }

    @PutMapping("/{codeFiliere}/anneeUniversitaire/{codeAnnee}")
    public ResponseEntity<FiliereResponse> addAnneeUniversitaireToFiliere(@PathVariable("codeFiliere") String codeFiliere,
                                                                          @PathVariable("codeAnnee") String codeAnnee) {

        Filiere filiere = filiereService.addAnneUnivToFiliere(codeFiliere, codeAnnee);
        FiliereResponse filiereResponse = filiereMapper.toFiliereResponse(filiere);

        return ResponseEntity
                .ok()
                .body(filiereResponse);
    }

    @DeleteMapping("/{codeFiliere}/anneeUniversitaire/{codeAnnee}")
    public ResponseEntity<?> removeAnneeUniversitaireFromFiliere(@PathVariable("codeFiliere") String codeFiliere,
                                                                 @PathVariable("codeAnnee") String codeAnnee) {

        filiereService.deleteAnneUnivFromFiliere(codeFiliere, codeAnnee);

        return ResponseEntity
                .noContent()
                .build();
    }


    @GetMapping("/exist/{codeFiliere}")
    public ResponseEntity<?> existByCodeFiliere(@PathVariable("codeFiliere") String codeFiliere) {
        filiereService.existByCodeFiliere(codeFiliere);

        return ResponseEntity
                .noContent()
                .build();
    }

    @GetMapping("/contains/{codeFiliere}")
    public ResponseEntity<List<FiliereResponse>> getFiliereContainig(@PathVariable("codeFiliere") String codeFiliere) {
        List<FiliereResponse> filiereResponseList = filiereMapper.toFiliereResponseList(filiereService.findByCodeFiliereContaining(codeFiliere));
        return ResponseEntity
                .ok()
                .body(filiereResponseList);
    }


    @PatchMapping("/{codeFiliere}")
    public ResponseEntity<FiliereResponse> update(
            @PathVariable("codeFiliere") String codeFiliere,
            @Valid @RequestBody FiliereUpdateRequest filiereUpdateRequest
    ) {

        Filiere filiere = filiereMapper.toFiliere(filiereUpdateRequest , codeFiliere);


        Filiere updatedFiliere = filiereService.update(filiere);
        FiliereResponse updatedDepartementResponse = filiereMapper.toFiliereResponse(updatedFiliere);

        return ResponseEntity
                .ok()
                .body(updatedDepartementResponse);
    }
}
