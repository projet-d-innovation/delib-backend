package ma.enset.utilisateur.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import ma.enset.utilisateur.dto.PagingResponse;
import ma.enset.utilisateur.dto.role.RoleCreateRequest;
import ma.enset.utilisateur.dto.role.RoleResponse;
import ma.enset.utilisateur.dto.role.RoleUpdateRequest;
import ma.enset.utilisateur.service.RoleService;
import org.hibernate.validator.constraints.Range;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/roles")
@AllArgsConstructor
@Validated
public class RoleController {

    private final RoleService service;

    @GetMapping
    public ResponseEntity<PagingResponse<RoleResponse>> findAll(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Range(min = 1, max = 100) int size,
            @RequestParam(defaultValue = "false") boolean includePermissions
    ) {

        return ResponseEntity
                .ok()
                .body(service.findAll(page, size, includePermissions));
    }

    @GetMapping("{code}")
    public ResponseEntity<RoleResponse> findByCode(
            @PathVariable String code,
            @RequestParam(defaultValue = "false") boolean includePermissions
    ) {
        return ResponseEntity
                .ok()
                .body(service.findById(code, includePermissions));
    }

    @GetMapping("/bulk")
    public ResponseEntity<Iterable<RoleResponse>> findByCodes(
            @NotEmpty @RequestParam Set<@NotBlank String> codes,
            @RequestParam(defaultValue = "false") boolean includePermissions
    ) {
        return ResponseEntity
                .ok()
                .body(service.findAllById(codes, includePermissions));
    }

    @PostMapping
    public ResponseEntity<RoleResponse> save(
            @Valid @RequestBody RoleCreateRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.save(request));
    }


    // TODO (ahmed) : fix this issue :
    // exemple :
    //    [ {
    //        "roleId": "striqsdqsdng",
    //            "roleName": "string",
    //            "permissions": [1]
    //    },
    //    {
    //        "roleId": "strqsdqsdqsing",
    //            "roleName": "string"
    //    }]
    // exception :
    //    {
    //        "code": 500,
    //        "status": "INTERNAL_SERVER_ERROR",
    //        "message": "The server encountered an internal error or misconfiguration and was unable to complete your request.",
    //        "identifiers": null,
    //        "errors": null
    //    }
    @PostMapping("/bulk")
    public ResponseEntity<List<RoleResponse>> saveAll(
            @RequestBody @NotEmpty List<@Valid RoleCreateRequest> roleRequests
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.saveAll(roleRequests));
    }


    @PatchMapping("/{code}")
    public ResponseEntity<RoleResponse> update(
            @PathVariable String code,
            @Valid @RequestBody RoleUpdateRequest roleUpdateRequest
    ) {
        return ResponseEntity
                .ok()
                .body(service.update(code, roleUpdateRequest));
    }

    @PatchMapping("/bulk")
    public ResponseEntity<List<RoleResponse>> updateAll(
            @RequestBody @NotEmpty List<@Valid RoleUpdateRequest> roleUpdateRequests
    ) {
        return ResponseEntity
                .ok()
                .body(service.updateAll(roleUpdateRequests));
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<Void> delete(
            @PathVariable String code
    ) {
        service.deleteById(code);
        return ResponseEntity
                .noContent()
                .build();
    }

    @DeleteMapping("/bulk")
    public ResponseEntity<Void> deleteAll(
            @RequestParam @NotEmpty Set<@NotBlank String> codes
    ) {
        service.deleteAllById(codes);
        return ResponseEntity
                .noContent()
                .build();
    }


}
