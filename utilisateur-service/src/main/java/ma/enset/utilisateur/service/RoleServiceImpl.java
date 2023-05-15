package ma.enset.utilisateur.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.utilisateur.constant.CoreConstants;
import ma.enset.utilisateur.dto.PagingResponse;
import ma.enset.utilisateur.dto.role.RoleCreateRequest;
import ma.enset.utilisateur.dto.role.RoleResponse;
import ma.enset.utilisateur.dto.role.RoleUpdateRequest;
import ma.enset.utilisateur.exception.DuplicateEntryException;
import ma.enset.utilisateur.exception.ElementAlreadyExistsException;
import ma.enset.utilisateur.exception.ElementNotFoundException;
import ma.enset.utilisateur.mapper.PermissionMapper;
import ma.enset.utilisateur.mapper.RoleMapper;
import ma.enset.utilisateur.model.Permission;
import ma.enset.utilisateur.model.Role;
import ma.enset.utilisateur.repository.RoleRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final String ELEMENT_TYPE = "Role";
    private final String ID_FIELD_NAME = "roleId";
    private final RoleRepository roleRepository;
    private final PermissionService permissionService;
    private final RoleMapper roleMapper;
    private final PermissionMapper permissionMapper;

    @Override
    public RoleResponse save(RoleCreateRequest roleCreateRequest) throws ElementAlreadyExistsException {

        roleRepository.findById(roleCreateRequest.roleId()).ifPresent(role -> {
            throw new ElementAlreadyExistsException(
                    CoreConstants.BusinessExceptionMessage.ALREADY_EXISTS,
                    new Object[]{ELEMENT_TYPE, ID_FIELD_NAME, roleCreateRequest.roleId()},
                    null
            );
        });

        Role role = roleMapper.toRole(roleCreateRequest);

        if (roleCreateRequest.permissions() != null && !roleCreateRequest.permissions().isEmpty()) {
            List<Permission> permissions = permissionMapper.fromResponseList(
                    permissionService.findAllById(
                            new HashSet<>(roleCreateRequest.permissions())
                    )
            );
            role.setPermissions(permissions);
        }

        Role createdRole;

        try {
            createdRole = roleRepository.save(role);
        } catch (DataIntegrityViolationException e) {
            throw new ElementAlreadyExistsException(
                    CoreConstants.BusinessExceptionMessage.ALREADY_EXISTS,
                    new Object[]{ELEMENT_TYPE, ID_FIELD_NAME, role.getRoleId()},
                    null
            );
        }

        return roleMapper.toRoleResponse(createdRole);
    }

    @Override
    @Transactional
    public List<RoleResponse> saveAll(List<RoleCreateRequest> roleCreateRequests) throws ElementAlreadyExistsException {

        Set<String> ids = roleCreateRequests.stream()
                .map(RoleCreateRequest::roleId)
                .collect(Collectors.toSet());

        if (ids.size() != roleCreateRequests.size()) {
            throw new DuplicateEntryException(
                    CoreConstants.BusinessExceptionMessage.DUPLICATE_ENTRY,
                    new Object[]{ELEMENT_TYPE}
            );
        }

        List<Role> roles = roleRepository.findAllById(ids);

        if (!roles.isEmpty()) {
            throw new ElementAlreadyExistsException(
                    CoreConstants.BusinessExceptionMessage.MANY_ALREADY_EXISTS,
                    new Object[]{ELEMENT_TYPE},
                    roles.stream()
                            .map(Role::getRoleId)
                            .collect(Collectors.toList())
            );
        }

        List<Role> roleList = roleMapper.toRoleList(roleCreateRequests);

        Set<Integer> permissionIdList = new HashSet<>();

        roleCreateRequests.forEach(
                roleCreateRequest -> {
                    if (roleCreateRequest.permissions() != null && !roleCreateRequest.permissions().isEmpty())
                        permissionIdList.addAll(roleCreateRequest.permissions());
                }
        );

        List<Permission> permissionList = permissionMapper.fromResponseList(
                permissionService.findAllById(permissionIdList)
        );

        for (int i = 0; i < roleList.size(); i++) {
            if (roleCreateRequests.get(i).permissions() == null
                    || roleCreateRequests.get(i).permissions().isEmpty())
                continue;
            Set<Integer> permsIds = roleCreateRequests.get(i).permissions();
            List<Permission> perms = permissionList.stream()
                    .filter(permission -> permsIds.contains(permission.getPermissionId()))
                    .toList();
            roleList.get(i).setPermissions(perms);
        }

        List<Role> createdRoleList;

        try {
            createdRoleList = roleRepository.saveAll(roleList);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEntryException(
                    CoreConstants.BusinessExceptionMessage.DUPLICATE_ENTRY,
                    null
            );
        }
        return roleMapper.toRoleResponseList(createdRoleList);
    }

    @Override
    public RoleResponse findById(String id, boolean includePermissions) throws ElementNotFoundException {

        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException(
                        CoreConstants.BusinessExceptionMessage.NOT_FOUND,
                        new Object[]{ELEMENT_TYPE, ID_FIELD_NAME, id},
                        null
                ));

        RoleResponse roleResponse = roleMapper.toRoleResponse(role);

        if (!includePermissions) {
            roleResponse.setPermissions(null);
        }

        return roleResponse;

    }

    @Override
    public List<RoleResponse> findAllById(Set<String> ids, boolean includePermissions) throws ElementNotFoundException {

        List<Role> roleList = roleRepository.findAllById(ids);
        List<String> foundIds = roleList.stream()
                .map(Role::getRoleId)
                .toList();

        if (foundIds.size() != ids.size()) {
            throw new ElementNotFoundException(
                    CoreConstants.BusinessExceptionMessage.MANY_NOT_FOUND,
                    new Object[]{ELEMENT_TYPE},
                    ids.stream()
                            .filter(id -> !foundIds.contains(id))
                            .toList()
            );
        }

        List<RoleResponse> roleResponseList = roleMapper.toRoleResponseList(
                roleList
        );

        if (!includePermissions && !roleResponseList.isEmpty()) {
            roleResponseList.forEach(
                    roleResponse -> {
                        roleResponse.setPermissions(null);
                    }
            );
        }

        return roleResponseList;
    }

    @Override
    public PagingResponse<RoleResponse> findAll(int page, int size, boolean includePermissions) {

        PagingResponse<RoleResponse> roleResponseList = roleMapper.toPagingResponse(
                roleRepository.findAll(PageRequest.of(page, size))
        );

        if (!includePermissions && !roleResponseList.records().isEmpty()) {
            roleResponseList.records().forEach(
                    roleResponse -> {
                        roleResponse.setPermissions(null);
                    }
            );
        }

        return roleResponseList;
    }

    @Override
    public RoleResponse update(String id, RoleUpdateRequest roleUpdateRequest) throws ElementNotFoundException {

        Role toBeUpdated = roleRepository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException(
                        CoreConstants.BusinessExceptionMessage.NOT_FOUND,
                        new Object[]{ELEMENT_TYPE, ID_FIELD_NAME, id},
                        null
                ));
        roleMapper.updateRequestToRole(roleUpdateRequest, toBeUpdated);
        toBeUpdated.setRoleId(id);

        if (roleUpdateRequest.permissions() != null && !roleUpdateRequest.permissions().isEmpty()) {
            List<Permission> permissions = permissionMapper.fromResponseList(
                    permissionService.findAllById(
                            roleUpdateRequest.permissions()
                    )
            );
            toBeUpdated.setPermissions(permissions);
        }

        return roleMapper.toRoleResponse(
                roleRepository.save(toBeUpdated)
        );
    }

    @Override
    @Transactional
    public List<RoleResponse> updateAll(List<RoleUpdateRequest> roleUpdateRequests) throws ElementNotFoundException {

        Set<String> ids = roleUpdateRequests.stream()
                .map(RoleUpdateRequest::roleId)
                .collect(Collectors.toSet());

        if (ids.size() != roleUpdateRequests.size()) {
            throw new DuplicateEntryException(
                    CoreConstants.BusinessExceptionMessage.DUPLICATE_ENTRY,
                    null
            );
        }

        List<Role> roleList = roleRepository.findAllById(ids);

        Set<String> foundIds = roleList.stream()
                .map(Role::getRoleId)
                .collect(Collectors.toSet());

        if (foundIds.size() != ids.size()) {
            throw new ElementNotFoundException(
                    CoreConstants.BusinessExceptionMessage.MANY_NOT_FOUND,
                    new Object[]{ELEMENT_TYPE},
                    ids.stream()
                            .filter(id -> !foundIds.contains(id))
                            .toList()
            );
        }

        roleMapper.updateRequestsToRoles(roleUpdateRequests, roleList);
        Set<Integer> permissionIds = new HashSet<>();

        roleUpdateRequests.forEach(
                roleUpdateRequest -> {
                    if (roleUpdateRequest.permissions() != null && !roleUpdateRequest.permissions().isEmpty())
                        permissionIds.addAll(roleUpdateRequest.permissions());
                }
        );

        if (!permissionIds.isEmpty()) {
            List<Permission> permissions = permissionMapper.fromResponseList(
                    permissionService.findAllById(permissionIds)
            );
            roleList.forEach(role -> {
                Set<Integer> permsIds = roleUpdateRequests.stream()
                        .filter(roleUpdateRequest -> roleUpdateRequest.roleId().equals(role.getRoleId()))
                        .map(RoleUpdateRequest::permissions)
                        .flatMap(Set::stream)
                        .collect(Collectors.toSet());
                List<Permission> perms = permissions.stream()
                        .filter(permission -> permsIds.contains(permission.getPermissionId()))
                        .toList();
                role.setPermissions(perms);
            });
        }

        return roleMapper.toRoleResponseList(
                roleRepository.saveAll(roleList)
        );
    }

    @Override
    public boolean exists(Set<String> roleIdList) {
        Set<String> foundIds = roleRepository.findAllById(roleIdList)
                .stream()
                .map(Role::getRoleId)
                .collect(Collectors.toSet());

        if (foundIds.size() != roleIdList.size())
            throw new ElementNotFoundException(
                    CoreConstants.BusinessExceptionMessage.MANY_NOT_FOUND,
                    new Object[]{ELEMENT_TYPE},
                    roleIdList.stream()
                            .filter(id -> !foundIds.contains(id))
                            .toList()
            );

        return true;
    }

    @Override
    public void deleteById(String id) throws ElementNotFoundException {
        if (!roleRepository.existsById(id))
            throw new ElementNotFoundException(
                    CoreConstants.BusinessExceptionMessage.NOT_FOUND,
                    new Object[]{ELEMENT_TYPE, ID_FIELD_NAME, id},
                    null
            );

        roleRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteAllById(Set<String> ids) throws ElementNotFoundException {
        this.exists(ids);
        roleRepository.deleteAllById(ids);
    }

}
