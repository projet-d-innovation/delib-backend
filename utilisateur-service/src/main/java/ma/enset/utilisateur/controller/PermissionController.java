package ma.enset.utilisateur.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import ma.enset.utilisateur.constant.CoreConstants;
import ma.enset.utilisateur.dto.PermissionCreateRequestDTO;
import ma.enset.utilisateur.dto.PermissionUpdateRequestDTO;
import ma.enset.utilisateur.dto.PermissionResponseDTO;
import ma.enset.utilisateur.model.Permission;
import ma.enset.utilisateur.service.PermissionService;
import ma.enset.utilisateur.util.PermissionMapper;
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


    @GetMapping("/code")
    public ResponseEntity<PermissionResponseDTO> findByCode(
            @RequestParam int code
    ) {
        Permission permission = permissionService.findById(code);
        PermissionResponseDTO permissionResponse = permissionMapper.toPermissionResponse(permission);

        return new ResponseEntity<>(
                permissionResponse,
                HttpStatus.OK
        );
    }

    @GetMapping("/code/many")
    public ResponseEntity<Iterable<PermissionResponseDTO>> findByCodes(
            @RequestParam List<Integer> codes
    ) {
        List<Permission> permissions = permissionService.findManyById(codes);
        List<PermissionResponseDTO> permissionResponses = permissionMapper.toPermissionResponses(permissions);

        return new ResponseEntity<>(
                permissionResponses,
                HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<PermissionResponseDTO> create(
            @Valid @RequestBody PermissionCreateRequestDTO permissionRequest
    ) {
        Permission permission = permissionMapper.toPermission(permissionRequest);
        Permission createdPermission = permissionService.create(permission);
        PermissionResponseDTO permissionResponse = permissionMapper.toPermissionResponse(createdPermission);

        return new ResponseEntity<>(
                permissionResponse,
                HttpStatus.CREATED
        );
    }

    @PostMapping("/many")
    public ResponseEntity<List<PermissionResponseDTO>> createMany(
            @Valid @RequestBody List<PermissionCreateRequestDTO> permissionRequests
    ) {
        List<Permission> permissions = permissionMapper.createToPermissions(permissionRequests);
        List<Permission> createdPermissions = permissionService.createMany(permissions);
        List<PermissionResponseDTO> permissionResponses = permissionMapper.toPermissionResponses(createdPermissions);

        return new ResponseEntity<>(
                permissionResponses,
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<Page<PermissionResponseDTO>> findAll(
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = CoreConstants.ValidationMessage.PAGINATION_PAGE_MIN)
            int page,

            @RequestParam(defaultValue = "20")
            @Min(value = 1, message = CoreConstants.ValidationMessage.PAGINATION_SIZE_MIN)
            @Max(value = 20, message = CoreConstants.ValidationMessage.PAGINATION_SIZE_MAX)
            int size
    ) {

        Pageable pageRequest = PageRequest.of(page, size);
        Page<Permission> elementsPage = permissionService.findAll(pageRequest);
        Page<PermissionResponseDTO> pagedResult = elementsPage.map(permissionMapper::toPermissionResponse);

        return new ResponseEntity<>(
                pagedResult,
                HttpStatus.OK
        );
    }

    @PutMapping
    public ResponseEntity<PermissionResponseDTO> update(
            @Valid @RequestBody PermissionUpdateRequestDTO permissionUpdateRequest
    ) {
        Permission permission = permissionMapper.toPermission(permissionUpdateRequest);
        Permission updatedPermission = permissionService.update(permission);
        PermissionResponseDTO permissionResponse = permissionMapper.toPermissionResponse(updatedPermission);
        // TODO : verify empty fields , so they will not be updated
        return new ResponseEntity<>(
                permissionResponse,
                HttpStatus.OK
        );
    }

    @PutMapping("/many")
    public ResponseEntity<List<PermissionResponseDTO>> updateMany(
            @Valid @RequestBody List<PermissionUpdateRequestDTO> permissionUpdateRequests
    ) {
        List<Permission> permissions = permissionMapper.updateToPermissions(permissionUpdateRequests);
        List<Permission> updatedPermissions = permissionService.updateMany(permissions);
        List<PermissionResponseDTO> permissionResponses = permissionMapper.toPermissionResponses(updatedPermissions);
        // TODO : verify empty fields , so they will not be updated
        return new ResponseEntity<>(
                permissionResponses,
                HttpStatus.OK
        );
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(
            @RequestParam int code
    ) {
        permissionService.deleteById(code);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/many")
    public ResponseEntity<Void> deleteMany(
            @RequestParam List<Integer> codes
    ) {
        permissionService.deleteManyById(codes);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
