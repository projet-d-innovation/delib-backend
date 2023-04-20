package ma.enset.filiereservice.contoller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import ma.enset.filiereservice.dto.FiliereCreationRequest;
import ma.enset.filiereservice.dto.FilierePagingResponse;
import ma.enset.filiereservice.dto.FiliereResponse;
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
    @GetMapping("/byCodeDeRegle/{codeRegle}")
    public ResponseEntity<List<FiliereResponse>> getByCodeRegle(@PathVariable("codeRegle") String codeRegle) {

        List<Filiere> foundFilieres = filiereService.findByCodeRegle(codeRegle);
        List<FiliereResponse> foundFiliereResponseList = filiereMapper.toFiliereResponseList(foundFilieres);

        return ResponseEntity
                .ok()
                .body(foundFiliereResponseList);
    }


    @GetMapping("/byCodeChef/{codeChef}")
    public ResponseEntity<FiliereResponse> getByCodeChefFiliere(@PathVariable("codeChef") String codeChef) {
        Filiere foundFiliere = filiereService.findByCodeChefFiliere(codeChef);

        FiliereResponse foundFiliereDeResponse = filiereMapper.toFiliereResponse(foundFiliere);

        return ResponseEntity
                .ok()
                .body(foundFiliereDeResponse);
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


}
