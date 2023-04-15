package ma.enset.utilisateur.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import ma.enset.utilisateur.dto.PagingResponse;
import ma.enset.utilisateur.dto.PermissionCreateRequestDTO;
import ma.enset.utilisateur.dto.PermissionResponseDTO;
import ma.enset.utilisateur.dto.PermissionUpdateRequestDTO;
import ma.enset.utilisateur.model.Permission;
import ma.enset.utilisateur.service.PermissionService;
import ma.enset.utilisateur.util.PermissionMapper;
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
@RequestMapping("/api/v1/utilisateurs/permissions")
@AllArgsConstructor
@Validated
public class PermissionController {

    private final PermissionMapper permissionMapper;
    private final PermissionService permissionService;


    @GetMapping("{code}")
    public ResponseEntity<PermissionResponseDTO> findByCode(
            @PathVariable Integer code
    ) {
        Permission permission = permissionService.findByPermissionId(code);
        PermissionResponseDTO permissionResponse = permissionMapper.toPermissionResponse(permission);

        return ResponseEntity
                .ok()
                .body(permissionResponse);
    }

    @GetMapping("/bulk")
    public ResponseEntity<Iterable<PermissionResponseDTO>> findByCodes(
            @NotEmpty @RequestParam List<Integer> codes
    ) {
        List<Permission> permissions = permissionService.findAllByPermissionId(codes);
        List<PermissionResponseDTO> permissionResponses = permissionMapper.toPermissionResponses(permissions);

        return ResponseEntity
                .ok()
                .body(permissionResponses);
    }

    @PostMapping
    public ResponseEntity<PermissionResponseDTO> save(
            @Valid @RequestBody PermissionCreateRequestDTO permissionRequest
    ) {
        Permission permission = permissionMapper.toPermission(permissionRequest);
        Permission savedPermission = permissionService.save(permission);
        PermissionResponseDTO permissionResponse = permissionMapper.toPermissionResponse(savedPermission);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(permissionResponse);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<PermissionResponseDTO>> saveAll(
            @NotEmpty @RequestBody List<@Valid PermissionCreateRequestDTO> permissionRequests
    ) {
        List<Permission> permissions = permissionMapper.createToPermissions(permissionRequests);
        List<Permission> savedPermissions = permissionService.saveAll(permissions);
        List<PermissionResponseDTO> permissionResponses = permissionMapper.toPermissionResponses(savedPermissions);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(permissionResponses);
    }

    @GetMapping
    public ResponseEntity<PagingResponse<PermissionResponseDTO>> findAll(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Range(min = 1, max = 10) int size
    ) {

        Pageable pageRequest = PageRequest.of(page, size);
        Page<Permission> elementsPage = permissionService.findAll(pageRequest);
        PagingResponse<PermissionResponseDTO> pagedResult = permissionMapper.toPagingResponse(elementsPage);

        return ResponseEntity
                .ok()
                .body(pagedResult);
    }

    @PatchMapping("/{code}")
    public ResponseEntity<PermissionResponseDTO> update(
            @PathVariable Integer code,
            @Valid @RequestBody PermissionUpdateRequestDTO permissionUpdateRequest
    ) {


        Permission permission = permissionService.findByPermissionId(code);

        permissionMapper.updateRequestToPermission(permissionUpdateRequest, permission);

        permission.setPermissionId(code);
        Permission updatedPermission = permissionService.update(permission);

        PermissionResponseDTO permissionResponse = permissionMapper.toPermissionResponse(updatedPermission);

        return ResponseEntity
                .ok()
                .body(permissionResponse);
    }

    @PatchMapping("/bulk")
    public ResponseEntity<List<PermissionResponseDTO>> updateAll(
            @RequestBody List<@Valid PermissionUpdateRequestDTO> permissionUpdateRequests
    ) {
        List<Integer> permissionIds = permissionMapper.toPermissionIds(permissionUpdateRequests);

        List<Permission> permissions = permissionService.findAllByPermissionId(permissionIds);

        permissionMapper.updateRequestsToPermissions(permissionUpdateRequests, permissions);

        List<Permission> updatedPermissions = permissionService.updateAll(permissions);

        List<PermissionResponseDTO> permissionResponses = permissionMapper.toPermissionResponses(updatedPermissions);

        return ResponseEntity
                .ok()
                .body(permissionResponses);
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<Void> delete(
            @PathVariable Integer code
    ) {
        permissionService.deleteByPermissionId(code);
        return ResponseEntity
                .ok()
                .build();
    }

    @DeleteMapping("/bulk")
    public ResponseEntity<Void> deleteAll(
            @RequestParam List<Integer> codes
    ) {
        permissionService.deleteAllByPermissionId(codes);
        return ResponseEntity
                .ok()
                .build();
    }


}
