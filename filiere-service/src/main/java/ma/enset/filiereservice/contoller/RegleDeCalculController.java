package ma.enset.filiereservice.contoller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import ma.enset.filiereservice.dto.RegleCreationRequest;
import ma.enset.filiereservice.dto.ReglePagingResponse;
import ma.enset.filiereservice.dto.RegleResponse;
import ma.enset.filiereservice.model.RegleDeCalcul;
import ma.enset.filiereservice.service.RegleDeCalculService;
import ma.enset.filiereservice.util.RegleDeCalculMapper;
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
@RequestMapping("/api/v1/regledecalcul")
public class RegleDeCalculController {
    private final RegleDeCalculService regleDeCalculService;
    private final RegleDeCalculMapper regleDeCalculMapper;

    @PostMapping
    public ResponseEntity<RegleResponse> save(@Valid @RequestBody RegleCreationRequest regleDeCalculCreationRequest) {
        RegleDeCalcul regleDeCalcul = regleDeCalculMapper.toRegleDeCalcul(regleDeCalculCreationRequest);
        RegleResponse regleDeCalculResponse = regleDeCalculMapper.toRegleDeCalculResponse(regleDeCalculService.save(regleDeCalcul));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(regleDeCalculResponse);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<RegleResponse>> saveAll(@RequestBody List<@Valid RegleCreationRequest> regleDeCalculCreationRequestList) {
        List<RegleDeCalcul> regleDeCalculList = regleDeCalculMapper.toRegleDeCalculList(regleDeCalculCreationRequestList);
        List<RegleResponse> regleDeCalculResponseList = regleDeCalculMapper.toRegleDeCalculResponseList(regleDeCalculService.saveAll(regleDeCalculList));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(regleDeCalculResponseList);
    }

    @GetMapping("/{codeRegleDeCalcul}")
    public ResponseEntity<RegleResponse> get(@PathVariable("codeRegleDeCalcul") String codeRegleDeCalcul) {

        RegleDeCalcul foundRegleDeCalcul = regleDeCalculService.findByCodeRegleDeCalcul(codeRegleDeCalcul);
        RegleResponse foundRegleDeCalculResponse = regleDeCalculMapper.toRegleDeCalculResponse(foundRegleDeCalcul);

        return ResponseEntity
                .ok()
                .body(foundRegleDeCalculResponse);
    }

    @GetMapping
    public ResponseEntity<ReglePagingResponse> getAll(@RequestParam(defaultValue = "0") @Min(0) int page,
                                                      @RequestParam(defaultValue = "10") @Range(min = 1, max = 10) int size) {

        Pageable pageRequest = PageRequest.of(page, size);
        Page<RegleDeCalcul> regleDeCalculPage = regleDeCalculService.findAll(pageRequest);
        ReglePagingResponse pagedResponse = regleDeCalculMapper.toPagingResponse(regleDeCalculPage);

        return ResponseEntity
                .ok()
                .body(pagedResponse);
    }


    @DeleteMapping("/{codeRegleDeCalcul}")
    public ResponseEntity<?> delete(@PathVariable("codeRegleDeCalcul") String codeRegleDeCalcul) {
        regleDeCalculService.deleteByCodeRegleDeCalcul(codeRegleDeCalcul);

        return ResponseEntity
                .noContent()
                .build();
    }

    @DeleteMapping("/bulk")
    public ResponseEntity<?> deleteAll(@RequestBody List<String> codeRegleDeCalculList) {
        regleDeCalculService.deleteAllByCodeRegleDeCalcul(codeRegleDeCalculList);

        return ResponseEntity
                .noContent()
                .build();
    }


}
