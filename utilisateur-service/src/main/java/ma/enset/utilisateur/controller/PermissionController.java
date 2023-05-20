package ma.enset.utilisateur.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import ma.enset.utilisateur.dto.PagingResponse;
import ma.enset.utilisateur.dto.permission.PermissionCreateRequest;
import ma.enset.utilisateur.dto.permission.PermissionResponse;
import ma.enset.utilisateur.dto.permission.PermissionUpdateRequest;
import ma.enset.utilisateur.service.PermissionService;
import org.hibernate.validator.constraints.Range;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/permissions")
@AllArgsConstructor
@Validated
public class PermissionController {
    private final PermissionService service;


    @GetMapping
    public ResponseEntity<PagingResponse<PermissionResponse>> findAll(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Range(min = 1, max = 100) int size
    ) {
        return ResponseEntity
                .ok()
                .body(service.findAll(page, size));
    }

    @GetMapping("{code}")
    public ResponseEntity<PermissionResponse> findByCode(
            @PathVariable Integer code
    ) {
        return ResponseEntity
                .ok()
                .body(service.findById(code));
    }

    @GetMapping("/bulk")
    public ResponseEntity<Iterable<PermissionResponse>> findByIdList(
            @NotEmpty @RequestParam Set<Integer> codes
    ) {
        return ResponseEntity
                .ok()
                .body(service.findAllById(codes));
    }

    @PostMapping
    public ResponseEntity<PermissionResponse> save(
            @Valid @RequestBody PermissionCreateRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.save(request));
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<PermissionResponse>> saveAll(
            @RequestBody @NotEmpty List<@Valid PermissionCreateRequest> requestList
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.saveAll(requestList));
    }


    @PatchMapping("/{code}")
    public ResponseEntity<PermissionResponse> update(
            @PathVariable Integer code,
            @Valid @RequestBody PermissionUpdateRequest request
    ) {
        return ResponseEntity
                .ok()
                .body(service.update(code, request));
    }

    @PatchMapping("/bulk")
    public ResponseEntity<List<PermissionResponse>> updateAll(
            @RequestBody @NotEmpty List<@Valid PermissionUpdateRequest> requestList
    ) {
        return ResponseEntity
                .ok()
                .body(service.updateAll(requestList));
    }

    @GetMapping("exist")
    public ResponseEntity<Boolean> exist(
            @RequestParam @NotEmpty Set<Integer> ids
    ) {
        service.exists(ids);
        return ResponseEntity
                .noContent()
                .build();
    }


    @DeleteMapping("/{code}")
    public ResponseEntity<Void> delete(
            @PathVariable Integer code
    ) {
        service.deleteById(code);
        return ResponseEntity
                .ok()
                .build();
    }

    @DeleteMapping("/bulk")
    public ResponseEntity<Void> deleteAll(
            @RequestParam @NotEmpty Set<Integer> codes
    ) {
        service.deleteAllById(codes);
        return ResponseEntity
                .ok()
                .build();
    }


}
