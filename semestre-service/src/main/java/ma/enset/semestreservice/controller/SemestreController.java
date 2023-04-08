package ma.enset.semestreservice.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import ma.enset.semestreservice.constant.CoreConstants;
import ma.enset.semestreservice.dto.SemestreRequestDTO;
import ma.enset.semestreservice.dto.SemestreResponseDTO;
import ma.enset.semestreservice.model.Semestre;
import ma.enset.semestreservice.service.SemestreServiceImpl;
import ma.enset.semestreservice.util.SemestreMapper;
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
    private final SemestreServiceImpl semestreServiceImpl;
    private final SemestreMapper semestreMapper;


    @PostMapping
    public ResponseEntity<SemestreResponseDTO> create(@Valid @RequestBody SemestreRequestDTO semestreRequest) {
        Semestre semestre = semestreMapper.toSemestre(semestreRequest);
        SemestreResponseDTO semestreResponse = semestreMapper.toSemestreResponse(semestreServiceImpl.create(semestre));

        return new ResponseEntity<>(
                semestreResponse,
                HttpStatus.CREATED
        );
    }

    @PostMapping("/many")
    public ResponseEntity<List<SemestreResponseDTO>> createMany(@Valid @RequestBody List<SemestreRequestDTO> semestreRequests) {
        List<Semestre> semestres = semestreMapper.toSemestres(semestreRequests);
        List<SemestreResponseDTO> semestreResponses = semestreMapper.toSemestreResponses(semestreServiceImpl.createMany(semestres));

        return new ResponseEntity<>(
                semestreResponses,
                HttpStatus.CREATED
        );
    }
    @PutMapping
    public ResponseEntity<SemestreResponseDTO> update(@Valid @RequestBody SemestreRequestDTO semestreRequest) {
        Semestre semestre = semestreMapper.toSemestre(semestreRequest);
        SemestreResponseDTO semestreResponses = semestreMapper.toSemestreResponse(semestreServiceImpl.update(semestre));

        return new ResponseEntity<>(
                semestreResponses,
                HttpStatus.OK
        );
    }
    @PutMapping("/many")
    public ResponseEntity<List<SemestreResponseDTO>> updateMany(@Valid @RequestBody List<SemestreRequestDTO> semestreRequests) {
        List<Semestre> semestres = semestreMapper.toSemestres(semestreRequests);
        List<SemestreResponseDTO> semestreResponses = semestreMapper.toSemestreResponses(semestreServiceImpl.updateMany(semestres));

        return new ResponseEntity<>(
                semestreResponses,
                HttpStatus.CREATED
        );
    }
    @DeleteMapping
    public ResponseEntity<Void> delete(@NotBlank @RequestParam String code) {
        semestreServiceImpl.deleteById(code);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @DeleteMapping("/many")
    public ResponseEntity<Void> deleteMany(@RequestParam List<String> code) {
        semestreServiceImpl.deleteManyById(code);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping
    public ResponseEntity<Page<SemestreResponseDTO>> findAll(
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = CoreConstants.ValidationMessage.PAGINATION_PAGE_MIN)
            int page,

            @RequestParam(defaultValue = "20")
            @Min(value = 1, message = CoreConstants.ValidationMessage.PAGINATION_SIZE_MIN)
            @Max(value = 20, message = CoreConstants.ValidationMessage.PAGINATION_SIZE_MAX)
            int size) {

        Pageable pageRequest = PageRequest.of(page, size);
        Page<Semestre> semestresPage = semestreServiceImpl.findAll(pageRequest);
        Page<SemestreResponseDTO> pagedResult = semestresPage.map(semestreMapper::toSemestreResponse);

        return new ResponseEntity<>(
                pagedResult,
                HttpStatus.OK
        );
    }
    @GetMapping("/code")
    public ResponseEntity<SemestreResponseDTO> findById(@RequestParam String code) {
        Semestre semestre = semestreServiceImpl.findById(code);
        SemestreResponseDTO elementResponse = semestreMapper.toSemestreResponse(semestre);

        return new ResponseEntity<>(
                elementResponse,
                HttpStatus.OK
        );
    }
    @GetMapping("/code/many")
    public ResponseEntity<List<SemestreResponseDTO>> findByIds(@RequestParam List<String> codes) {
        List<Semestre> semestres = semestreServiceImpl.findManyById(codes);
        List<SemestreResponseDTO> semestreResponseDTOS = semestres.stream()
                .map(semestreMapper::toSemestreResponse)
                .toList();

        return new ResponseEntity<>(
                semestreResponseDTOS,
                HttpStatus.OK
        );
    }
    @GetMapping("/filiere/code")
    public ResponseEntity<Boolean> existsByCodeFiliere(@RequestParam String code) {
        Boolean exists = semestreServiceImpl.existsByCodeFiliere(code);

        return new ResponseEntity<>(
                exists,
                HttpStatus.OK
        );
    }






}
