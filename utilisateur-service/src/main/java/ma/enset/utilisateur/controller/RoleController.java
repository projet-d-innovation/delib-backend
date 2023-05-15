//package ma.enset.utilisateur.controller;
//
//import jakarta.validation.Valid;
//import jakarta.validation.constraints.Min;
//import jakarta.validation.constraints.NotEmpty;
//import lombok.AllArgsConstructor;
//import ma.enset.utilisateur.dto.PagingResponse;
//import ma.enset.utilisateur.dto.role.RoleCreateRequestDTO;
//import ma.enset.utilisateur.dto.role.RoleResponseDTO;
//import ma.enset.utilisateur.dto.role.RoleUpdateRequestDTO;
//import ma.enset.utilisateur.model.Role;
//import ma.enset.utilisateur.service.RoleService;
//import ma.enset.utilisateur.util.RoleMapper;
//import org.hibernate.validator.constraints.Range;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/v1/utilisateurs/roles")
//@AllArgsConstructor
//@Validated
//public class RoleController {
//
//    private final RoleMapper roleMapper;
//    private final RoleService roleService;
//
//
//    @GetMapping("{code}")
//    public ResponseEntity<RoleResponseDTO> findByCode(
//            @PathVariable String code
//    ) {
//        Role role = roleService.findByRoleId(code);
//
//        RoleResponseDTO roleResponse = roleMapper.toRoleResponse(role);
//
//        return ResponseEntity
//                .ok()
//                .body(roleResponse);
//    }
//
//    @GetMapping("/bulk")
//    public ResponseEntity<Iterable<RoleResponseDTO>> findByCodes(
//            @NotEmpty @RequestParam List<String> codes
//    ) {
//        List<Role> roles = roleService.findAllByRoleId(codes);
//        List<RoleResponseDTO> roleResponses = roleMapper.toRoleResponses(roles);
//
//        return ResponseEntity
//                .ok()
//                .body(roleResponses);
//    }
//
//    @PostMapping
//    public ResponseEntity<RoleResponseDTO> save(
//            @Valid @RequestBody RoleCreateRequestDTO roleRequest
//    ) {
//        Role role = roleMapper.toRole(roleRequest);
//        Role savedRole = roleService.save(role);
//        RoleResponseDTO roleResponse = roleMapper.toRoleResponse(savedRole);
//
//        return ResponseEntity
//                .status(HttpStatus.CREATED)
//                .body(roleResponse);
//    }
//
//    @PostMapping("/bulk")
//    public ResponseEntity<List<RoleResponseDTO>> saveAll(
//            @RequestBody List<@Valid RoleCreateRequestDTO> roleRequests
//    ) {
//        List<Role> roles = roleMapper.createToRoles(roleRequests);
//        List<Role> savedRoles = roleService.saveAll(roles);
//        List<RoleResponseDTO> roleResponses = roleMapper.toRoleResponses(savedRoles);
//
//        return ResponseEntity
//                .status(HttpStatus.CREATED)
//                .body(roleResponses);
//    }
//
//    @GetMapping
//    public ResponseEntity<PagingResponse<RoleResponseDTO>> findAll(
//            @RequestParam(defaultValue = "0") @Min(0) int page,
//            @RequestParam(defaultValue = "10") @Range(min = 1, max = 100) int size
//    ) {
//
//        Pageable pageRequest = PageRequest.of(page, size);
//        Page<Role> elementsPage = roleService.findAll(pageRequest);
//
//        PagingResponse<RoleResponseDTO> pagedResult = roleMapper.toPagingResponse(elementsPage);
//
//        return ResponseEntity
//                .ok()
//                .body(pagedResult);
//    }
//
//    @PatchMapping("/{code}")
//    public ResponseEntity<RoleResponseDTO> update(
//            @PathVariable String code,
//            @Valid @RequestBody RoleUpdateRequestDTO roleUpdateRequest
//    ) {
//
//        Role role = roleService.findByRoleId(code);
//
//        roleMapper.updateRequestToRole(roleUpdateRequest, role);
//
//        role.setRoleId(code);
//
//        Role updatedRole = roleService.update(role);
//
//        RoleResponseDTO roleResponse = roleMapper.toRoleResponse(updatedRole);
//
//        return ResponseEntity
//                .ok()
//                .body(roleResponse);
//    }
//
//    @PatchMapping("/bulk")
//    public ResponseEntity<List<RoleResponseDTO>> updateAll(
//            @RequestBody List<@Valid RoleUpdateRequestDTO> roleUpdateRequests
//    ) {
//
//        List<String> ids = roleMapper.toRoleIds(roleUpdateRequests);
//
//        List<Role> roles = roleService.findAllByRoleId(ids);
//
//        roleMapper.updateRequestsToRoles(roleUpdateRequests, roles);
//
//        List<Role> updatedRoles = roleService.updateAll(roles);
//
//        List<RoleResponseDTO> roleResponses = roleMapper.toRoleResponses(updatedRoles);
//
//        return ResponseEntity
//                .ok()
//                .body(roleResponses);
//    }
//
//    @DeleteMapping("/{code}")
//    public ResponseEntity<Void> delete(
//            @PathVariable String code
//    ) {
//        roleService.deleteByRoleId(code);
//        return ResponseEntity
//                .noContent()
//                .build();
//    }
//
//    @DeleteMapping("/bulk")
//    public ResponseEntity<Void> deleteAll(
//            @NotEmpty @RequestParam List<String> codes
//    ) {
//        roleService.deleteAllByRoleId(codes);
//        return ResponseEntity
//                .noContent()
//                .build();
//    }
//
//
//}
