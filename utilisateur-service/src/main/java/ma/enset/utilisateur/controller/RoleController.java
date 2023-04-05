package ma.enset.utilisateur.controller;

import io.micrometer.common.lang.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import ma.enset.utilisateur.constant.CoreConstants;
import ma.enset.utilisateur.dto.RoleCreateRequestDTO;
import ma.enset.utilisateur.dto.RoleResponseDTO;
import ma.enset.utilisateur.dto.RoleUpdateRequestDTO;
import ma.enset.utilisateur.model.Role;
import ma.enset.utilisateur.service.RoleService;
import ma.enset.utilisateur.util.RoleMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/utilisateurs/roles")
@AllArgsConstructor
@Validated
public class RoleController {

    private final RoleMapper roleMapper;
    private final RoleService roleService;


    @GetMapping("/code")
    public ResponseEntity<RoleResponseDTO> findByCode(
            @RequestParam String code,
            @Nullable @RequestParam(defaultValue = "false") boolean includePerms
    ) {
        Role role = roleService.findById(code);

        RoleResponseDTO roleResponse;

        if (includePerms)
            roleResponse = roleMapper.toRoleWithPermsResponse(role);
        else
            roleResponse = roleMapper.toRoleWithoutPermsResponse(role);


        return new ResponseEntity<>(
                roleResponse,
                HttpStatus.OK
        );
    }

    @GetMapping("/code/many")
    public ResponseEntity<Iterable<RoleResponseDTO>> findByCodes(
            @RequestParam List<String> codes,
            @Nullable @RequestParam(defaultValue = "false") boolean includePerms
    ) {
        List<Role> roles = roleService.findManyById(codes);
        List<RoleResponseDTO> roleResponses;
        if (includePerms)
            roleResponses = roleMapper.toRoleWithPermsResponses(roles);
        else
            roleResponses = roleMapper.toRoleWithoutPermsResponses(roles);

        return new ResponseEntity<>(
                roleResponses,
                HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<RoleResponseDTO> create(
            @Valid @RequestBody RoleCreateRequestDTO roleRequest
    ) {
        Role role = roleMapper.toRole(roleRequest);
        Role createdRole = roleService.create(role);
        RoleResponseDTO roleResponse = roleMapper.toRoleWithPermsResponse(createdRole);

        return new ResponseEntity<>(
                roleResponse,
                HttpStatus.CREATED
        );
    }

    @PostMapping("/many")
    public ResponseEntity<List<RoleResponseDTO>> createMany(
            @Valid @RequestBody List<RoleCreateRequestDTO> roleRequests
    ) {
        List<Role> roles = roleMapper.createToRoles(roleRequests);
        List<Role> createdRoles = roleService.createMany(roles);
        List<RoleResponseDTO> roleResponses = roleMapper.toRoleWithPermsResponses(createdRoles);

        return new ResponseEntity<>(
                roleResponses,
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<Page<RoleResponseDTO>> findAll(
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = CoreConstants.ValidationMessage.PAGINATION_PAGE_MIN)
            int page,

            @RequestParam(defaultValue = "20")
            @Min(value = 1, message = CoreConstants.ValidationMessage.PAGINATION_SIZE_MIN)
            @Max(value = 20, message = CoreConstants.ValidationMessage.PAGINATION_SIZE_MAX)
            int size,
            @Nullable @RequestParam(defaultValue = "false") boolean includePerms
    ) {

        Pageable pageRequest = PageRequest.of(page, size);
        Page<Role> elementsPage = roleService.findAll(pageRequest);

        Page<RoleResponseDTO> pagedResult;
        if (includePerms)
            pagedResult = elementsPage.map(roleMapper::toRoleWithPermsResponse);
        else
            pagedResult = elementsPage.map(roleMapper::toRoleWithoutPermsResponse);
        return new ResponseEntity<>(
                pagedResult,
                HttpStatus.OK
        );
    }

    @PutMapping
    public ResponseEntity<RoleResponseDTO> update(
            @Valid @RequestBody RoleUpdateRequestDTO roleUpdateRequest
    ) {
        Role role = roleMapper.toRole(roleUpdateRequest);
        Role updatedRole = roleService.update(role);
        RoleResponseDTO roleResponse = roleMapper.toRoleWithPermsResponse(updatedRole);
        return new ResponseEntity<>(
                roleResponse,
                HttpStatus.OK
        );
    }

    @PutMapping("/many")
    public ResponseEntity<List<RoleResponseDTO>> updateMany(
            @Valid @RequestBody List<RoleUpdateRequestDTO> roleUpdateRequests
    ) {
        List<Role> roles = roleMapper.updateToRoles(roleUpdateRequests);
        List<Role> updatedRoles = roleService.updateMany(roles);
        List<RoleResponseDTO> roleResponses = roleMapper.toRoleWithPermsResponses(updatedRoles);
        return new ResponseEntity<>(
                roleResponses,
                HttpStatus.OK
        );
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(
            @RequestParam String code
    ) {
        roleService.deleteById(code);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/many")
    public ResponseEntity<Void> deleteMany(
            @RequestParam List<String> codes
    ) {
        roleService.deleteManyById(codes);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
