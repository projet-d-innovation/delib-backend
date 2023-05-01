package ma.enset.semestreservice.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import ma.enset.semestreservice.dto.SemestreCreationRequest;
import ma.enset.semestreservice.dto.SemestrePagingResponse;
import ma.enset.semestreservice.dto.SemestreResponse;
import ma.enset.semestreservice.dto.SemestreUpdateRequest;
import ma.enset.semestreservice.model.Semestre;
import ma.enset.semestreservice.service.SemestreService;
import ma.enset.semestreservice.util.SemestreMapper;
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
@Validated
@AllArgsConstructor
@RequestMapping("/api/v1/semestres")
public class SemestreController {
    private final SemestreService semestreService;
    private final SemestreMapper semestreMapper;



    @PostMapping
    public ResponseEntity<SemestreResponse> save(@Valid @RequestBody SemestreCreationRequest semestreCreationRequest) {
        Semestre semestre = semestreMapper.toSemestre(semestreCreationRequest);
        SemestreResponse semestreResponse = semestreMapper.toSemestreResponse(semestreService.save(semestre));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(semestreResponse);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<SemestreResponse>> saveAll(@RequestBody List<@Valid SemestreCreationRequest> semestreCreationRequestList) {
        List<Semestre> semestreList = semestreMapper.toSemestreList(semestreCreationRequestList);
        List<SemestreResponse> semestreResponseList = semestreMapper.toSemestreResponseList(semestreService.saveAll(semestreList));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(semestreResponseList);
    }

    @GetMapping("/{codeSemestre}")
    public ResponseEntity<SemestreResponse> get(@PathVariable("codeSemestre") String codeSemestre) {

        Semestre foundSemestre = semestreService.findByCodeSemestre(codeSemestre);
        SemestreResponse foundSemestreResponse = semestreMapper.toSemestreResponse(foundSemestre);

        return ResponseEntity
                .ok()
                .body(foundSemestreResponse);
    }

    @GetMapping
    public ResponseEntity<SemestrePagingResponse> getAll(@RequestParam(defaultValue = "0") @Min(0) int page,
                                                         @RequestParam(defaultValue = "10") @Range(min = 1, max = 10) int size) {

        Pageable pageRequest = PageRequest.of(page, size);
        Page<Semestre> semestrePage = semestreService.findAll(pageRequest);
        SemestrePagingResponse pagedResponse = semestreMapper.toPagingResponse(semestrePage);

        return ResponseEntity
                .ok()
                .body(pagedResponse);
    }
    @GetMapping("/exist/{codeSemestre}")
    public ResponseEntity<Boolean> doesSemesterExist(@PathVariable("codeSemestre") String codeSemestre){
        boolean doesSemesterExist = semestreService.doesSemesterExist(codeSemestre);
        return ResponseEntity
                .ok()
                .body(doesSemesterExist);

    }


    @PatchMapping("/{codeSemestre}")
    public ResponseEntity<SemestreResponse> update(
            @PathVariable("codeSemestre") String codeSemestre,
            @Valid @RequestBody SemestreUpdateRequest semestreUpdateRequest
    ) {

        Semestre semestre = semestreService.findByCodeSemestre(codeSemestre);
        semestreMapper.updateSemestreFromDTO(semestreUpdateRequest, semestre);

        Semestre updatedSemestre = semestreService.update(semestre);
        SemestreResponse updatedSemestreResponse = semestreMapper.toSemestreResponse(updatedSemestre);

        return ResponseEntity
                .ok()
                .body(updatedSemestreResponse);
    }

    @DeleteMapping("/{codeSemestre}")
    public ResponseEntity<?> delete(@PathVariable("codeSemestre") String codeSemestre) {
        semestreService.deleteByCodeSemestre(codeSemestre);

        return ResponseEntity
                .noContent()
                .build();
    }

    @DeleteMapping("/bulk")
    public ResponseEntity<?> deleteAll(@RequestBody List<String> codeSemestreList) {
        semestreService.deleteAllByCodeSemestre(codeSemestreList);

        return ResponseEntity
                .noContent()
                .build();
    }

    @PutMapping("/{codeSemestre}/module/{codeModule}")
    public ResponseEntity<SemestreResponse> addModuleToSemestre(@PathVariable("codeSemestre") String codeSemestre,
                                                                          @PathVariable("codeModule") String codeModule) {

        Semestre semestre = semestreService.addModuleToSemestre(codeSemestre, codeModule);
        SemestreResponse  semestreResponse = semestreMapper.toSemestreResponse(semestre);

        return ResponseEntity
                .ok()
                .body(semestreResponse);
    }
    @PutMapping("/{codeSemestre}/session/{codeSession}")
    public ResponseEntity<SemestreResponse> addSessionToSemestre(@PathVariable("codeSemestre") String codeSemestre,
                                                                @PathVariable("codeSession") String codeSession) {

        Semestre semestre = semestreService.addSessionToSemestre(codeSemestre, codeSession);
        SemestreResponse  semestreResponse = semestreMapper.toSemestreResponse(semestre);
        return ResponseEntity
                .ok()
                .body(semestreResponse);
    }

    @DeleteMapping("/{codeSemestre}/module/{codeModule}")
    public ResponseEntity<?> removeModuleFromSemestre(@PathVariable("codeSemestre") String codeSemestre,
                                                                 @PathVariable("codeModule") String codeModule) {

        semestreService.removeModuleFromSemestre(codeSemestre, codeModule);

        return ResponseEntity
                .noContent()
                .build();
    }

    @DeleteMapping("/{codeSemestre}/session/{codeSession}")
    public ResponseEntity<?> removeSessionFromSemestre(@PathVariable("codeSemestre") String codeSemestre,
                                                      @PathVariable("codeSession") String codeSession) {

        semestreService.removeSessionFromSemestre(codeSemestre, codeSession);
        return ResponseEntity
                .noContent()
                .build();
    }







}
