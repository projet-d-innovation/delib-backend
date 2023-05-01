package ma.enset.departementservice.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import ma.enset.departementservice.dto.DepartementCreationRequest;
import ma.enset.departementservice.dto.DepartementPagingResponse;
import ma.enset.departementservice.dto.DepartementResponse;
import ma.enset.departementservice.dto.DepartementUpdateRequest;
import ma.enset.departementservice.model.Departement;
import ma.enset.departementservice.proxy.UserFeignClient;
import ma.enset.departementservice.service.DepartementService;
import ma.enset.departementservice.util.DepartementMapper;
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
@RequestMapping("/api/v1/departements")
public class DepartementController {
    private final DepartementService departementService;
    private final DepartementMapper departementMapper;
    private final UserFeignClient userFeignClient;

    @PostMapping
    public ResponseEntity<DepartementResponse> save(@Valid @RequestBody DepartementCreationRequest departementCreationRequest) {
        Departement departement = departementMapper.toDepartement(departementCreationRequest, departementService, userFeignClient);
        DepartementResponse departementResponse = departementMapper.toDepartementResponse(departementService.save(departement));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(departementResponse);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<DepartementResponse>> saveAll(@RequestBody List<@Valid DepartementCreationRequest> departementCreationRequestList) {
        List<Departement> departementList = departementCreationRequestList.stream()
                .map(departementCreationRequest ->
                        departementMapper.toDepartement(departementCreationRequest, departementService, userFeignClient)).toList();
        List<DepartementResponse> departementResponseList = departementMapper.toDepartementResponseList(departementService.saveAll(departementList));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(departementResponseList);
    }

    @GetMapping("/{codeDepartement}")
    public ResponseEntity<DepartementResponse> get(@PathVariable("codeDepartement") String codeDepartement) {

        Departement foundDepartement = departementService.findByCodeDepartement(codeDepartement);
        DepartementResponse foundDepartementResponse = departementMapper.toDepartementResponse(foundDepartement);

        return ResponseEntity
                .ok()
                .body(foundDepartementResponse);
    }

    @GetMapping("/exist/{codeDepartement}")
    public ResponseEntity<?> doesDepartmentExist(@PathVariable("codeDepartement") String codeDepartement) {
        departementService.existByCodeDepartement(codeDepartement);
        return ResponseEntity
                .noContent()
                .build();
    }

    @GetMapping("/contains/{codeDepartement}")
    public ResponseEntity<List<DepartementResponse>> getDepartmentContainig(@PathVariable("codeDepartement") String codeDepartement) {
        List<DepartementResponse> departementResponseList = departementMapper.toDepartementResponseList(departementService.findByCodeDepartementContaining(codeDepartement));
        return ResponseEntity
                .ok()
                .body(departementResponseList);
    }

    @GetMapping
    public ResponseEntity<DepartementPagingResponse> getAll(@RequestParam(defaultValue = "0") @Min(0) int page,
                                                            @RequestParam(defaultValue = "10") @Range(min = 1, max = 10) int size) {

        Pageable pageRequest = PageRequest.of(page, size);
        Page<Departement> departementPage = departementService.findAll(pageRequest);
        DepartementPagingResponse pagedResponse = departementMapper.toPagingResponse(departementPage);

        return ResponseEntity
                .ok()
                .body(pagedResponse);
    }

    @PatchMapping("/{codeDepartement}")
    public ResponseEntity<DepartementResponse> update(
            @PathVariable("codeDepartement") String codeDepartement,
            @Valid @RequestBody DepartementUpdateRequest departementUpdateRequest
    ) {

        Departement departement = departementService.findByCodeDepartement(codeDepartement);
        departementMapper.updateDepartementFromDTO(departementUpdateRequest, departement);

        Departement updatedDepartement = departementService.update(departement);
        DepartementResponse updatedDepartementResponse = departementMapper.toDepartementResponse(updatedDepartement);

        return ResponseEntity
                .ok()
                .body(updatedDepartementResponse);
    }

    @DeleteMapping("/{codeDepartement}")
    public ResponseEntity<?> delete(@PathVariable("codeDepartement") String codeDepartement) {
        departementService.deleteByCodeDepartement(codeDepartement);

        return ResponseEntity
                .noContent()
                .build();
    }

    @DeleteMapping("/bulk")
    public ResponseEntity<?> deleteAll(@RequestBody List<String> codeDepartementList) {
        departementService.deleteAllByCodeDepartement(codeDepartementList);

        return ResponseEntity
                .noContent()
                .build();
    }


    @PutMapping("/{codeDepartement}/filiere/{codeFiliere}")
    public ResponseEntity<DepartementResponse> addFiliereToDepartment(@PathVariable("codeDepartement") String codeDepartement,
                                                                      @PathVariable("codeFiliere") String codeFiliere) {
        DepartementResponse departementResponse = departementMapper.toDepartementResponse(departementService.pushFiliereToDepartment(codeDepartement, codeFiliere));
        return ResponseEntity
                .ok(departementResponse);
    }

    @PutMapping("/{codeDepartement}/utilisateur/{codeUser}")
    public ResponseEntity<DepartementResponse> addUserToDepartment(@PathVariable("codeDepartement") String codeDepartement, @PathVariable("codeUser") String codeUser) {
        DepartementResponse departementResponse = departementMapper.toDepartementResponse(departementService.pushUserToDepartment(codeDepartement, codeUser));
        return ResponseEntity
                .ok(departementResponse);
    }

    @PutMapping("/{codeDepartement}/utilisateur/bulk")
    public ResponseEntity<?> addUsersToDepartment(@PathVariable("codeDepartement") String codeDepartement, @RequestBody List<String> usersCode) {
        departementService.pushUsersToDepartment(codeDepartement, usersCode);
        return ResponseEntity
                .noContent()
                .build();
    }


    @DeleteMapping("/{codeDepartement}/utilisateur/{codeUser}")
    public ResponseEntity<?> deleteUserInDepartment(@PathVariable("codeDepartement") String codeDepartement, @PathVariable("codeUser") String codeUser) {
        departementService.deleteUserFromDepartment(codeUser, codeDepartement);
        return ResponseEntity
                .noContent()
                .build();
    }

    @DeleteMapping("/{codeDepartement}/filiere/{codeFiliere}")
    public ResponseEntity<?> deleteFiliereInDepartment(@PathVariable("codeDepartement") String codeDepartement, @PathVariable("codeFiliere") String codeFiliere) {
        departementService.deleteFiliereFromDepartment(codeFiliere, codeDepartement);
        return ResponseEntity
                .noContent()
                .build();
    }

    @GetMapping("/exist/{codeDepartement}/utilisateurs/{codeUser}")
    public ResponseEntity<?> isUserInDepartment(@PathVariable("codeDepartement") String codeDepartement, @PathVariable("codeUser") String codeUser) {
        departementService.isUserInDepartment(codeUser, codeDepartement);
        return ResponseEntity
                .noContent()
                .build();
    }
}
