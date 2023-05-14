package ma.enset.filiereservice.contoller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import ma.enset.filiereservice.dto.RegleDeCalculCreationRequest;
import ma.enset.filiereservice.dto.RegleDeCalculPagingResponse;
import ma.enset.filiereservice.dto.RegleDeCalculResponse;
import ma.enset.filiereservice.dto.RegleDeCalculUpdateRequest;
import ma.enset.filiereservice.service.RegleDeCalculService;
import org.hibernate.validator.constraints.Range;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;


@RestController
@Validated
@AllArgsConstructor
@RequestMapping("/api/v1/regledecalcul")
public class RegleDeCalculController {
    private final RegleDeCalculService regleDeCalculService;


    @PostMapping
    public ResponseEntity<RegleDeCalculResponse> save(@Valid @RequestBody RegleDeCalculCreationRequest regleDeCalculCreationRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(regleDeCalculService.save(regleDeCalculCreationRequest));
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<RegleDeCalculResponse>> saveAll(@RequestBody @NotEmpty List<@Valid RegleDeCalculCreationRequest> regleDeCalculCreationRequests) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(regleDeCalculService.saveAll(regleDeCalculCreationRequests));
    }

    @GetMapping("/{codeRegleDeCalcul}")
    public ResponseEntity<RegleDeCalculResponse> getById(@PathVariable("codeRegleDeCalcul") String codeRegleDeCalcul) {
        return ResponseEntity
                .ok()
                .body(regleDeCalculService.findById(codeRegleDeCalcul));
    }

    @GetMapping("/bulk")
    public ResponseEntity<List<RegleDeCalculResponse>> getAllById(@RequestParam @NotEmpty Set<@NotBlank String> codeRegleDeCalculList) {
        return ResponseEntity
                .ok()
                .body(regleDeCalculService.findAllById(codeRegleDeCalculList));
    }

    @GetMapping
    public ResponseEntity<RegleDeCalculPagingResponse> getAll(@RequestParam(defaultValue = "0") @Min(0) int page,
                                                              @RequestParam(defaultValue = "10") @Range(min = 1, max = 10) int size
    ) {
        return ResponseEntity
                .ok()
                .body(regleDeCalculService.findAll(page, size));
    }

    @PatchMapping("/{codeRegleDeCalcul}")
    public ResponseEntity<RegleDeCalculResponse> update(@PathVariable String codeRegleDeCalcul,
                                                        @Valid @RequestBody RegleDeCalculUpdateRequest regleDeCalculUpdateRequest) {
        return ResponseEntity
                .ok()
                .body(regleDeCalculService.update(codeRegleDeCalcul, regleDeCalculUpdateRequest));
    }


    @DeleteMapping("/{codeRegleDeCalcul}")
    public ResponseEntity<?> delete(@PathVariable String codeRegleDeCalcul) {
        regleDeCalculService.deleteById(codeRegleDeCalcul);
        return ResponseEntity
                .noContent()
                .build();
    }

    @DeleteMapping("/bulk")
    public ResponseEntity<?> deleteById(@RequestParam @NotEmpty Set<@NotBlank String> codeRegleDeCalcul) {
        regleDeCalculService.deleteById(codeRegleDeCalcul);
        return ResponseEntity
                .noContent()
                .build();
    }


}
