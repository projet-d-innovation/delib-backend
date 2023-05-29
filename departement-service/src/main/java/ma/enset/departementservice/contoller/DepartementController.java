package ma.enset.departementservice.contoller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import ma.enset.departementservice.dto.DepartementCreationRequest;
import ma.enset.departementservice.dto.DepartementPagingResponse;
import ma.enset.departementservice.dto.DepartementResponse;
import ma.enset.departementservice.dto.DepartementUpdateRequest;
import ma.enset.departementservice.service.DepartementService;
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
@RequestMapping("/api/v1/departements")
public class DepartementController {
    private final DepartementService departementService;

    @PostMapping
    public ResponseEntity<DepartementResponse> save(@Valid @RequestBody DepartementCreationRequest departementCreationRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(departementService.save(departementCreationRequest));
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<DepartementResponse>> saveAll(@RequestBody @NotEmpty List<@NotNull @Valid DepartementCreationRequest> departementCreationRequestList) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(departementService.saveAll(departementCreationRequestList));
    }

    @GetMapping
    public ResponseEntity<DepartementPagingResponse> getAll(@RequestParam(defaultValue = "") String search,
                                                            @RequestParam(defaultValue = "0") @Min(0) int page,
                                                            @RequestParam(defaultValue = "10") @Range(min = 1, max = 100) int size,
                                                            @RequestParam(defaultValue = "false") boolean includeFilieres,
                                                            @RequestParam(defaultValue = "false") boolean includeChefDepartement
    ) {
        return ResponseEntity.ok(
                departementService.findAll(page, size, search, includeFilieres, includeChefDepartement)
        );
    }

    @GetMapping("/{codeDepartement}")
    public ResponseEntity<DepartementResponse> get(@PathVariable("codeDepartement") String codeDepartement,
                                                   @RequestParam(defaultValue = "false") boolean includeFilieres,
                                                   @RequestParam(defaultValue = "false") boolean includeChefDepartement
    ) {
        return ResponseEntity.ok(
                departementService.findById(codeDepartement, includeFilieres, includeChefDepartement)
        );
    }

    @GetMapping("/bulk")
    public ResponseEntity<List<DepartementResponse>> getAllById(@NotEmpty @RequestParam Set<@NotBlank String> codeDepartementList,
                                                                @RequestParam(defaultValue = "false") boolean includeFilieres,
                                                                @RequestParam(defaultValue = "false") boolean includeChefDepartement
    ) {
        return ResponseEntity.ok(
                departementService.findAllById(codeDepartementList, includeFilieres, includeChefDepartement)
        );
    }


    @GetMapping("/exists")
    public ResponseEntity<Void> existsAll(@RequestParam @NotEmpty Set<@NotBlank String> codesDepartement) {
        departementService.existsAllId(codesDepartement);
        return ResponseEntity.noContent().build();
    }


    @PatchMapping("/{codeDepartement}")
    public ResponseEntity<DepartementResponse> update(
            @PathVariable("codeDepartement") String codeDepartement,
            @Valid @RequestBody DepartementUpdateRequest departementUpdateRequest
    ) {
        return ResponseEntity.ok(
                departementService.update(codeDepartement, departementUpdateRequest)
        );
    }

    @DeleteMapping("/{codeDepartement}")
    public ResponseEntity<?> delete(@PathVariable("codeDepartement") String codeDepartement) {
        departementService.deleteById(codeDepartement);
        return ResponseEntity
                .noContent()
                .build();
    }

    @DeleteMapping("/bulk")
    public ResponseEntity<?> deleteAll(@RequestParam @NotEmpty Set<@NotBlank String> codeDepartementList) {
        departementService.deleteById(codeDepartementList);
        return ResponseEntity
                .noContent()
                .build();
    }


}
