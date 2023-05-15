//package ma.enset.utilisateur.service;
//
//import jakarta.transaction.Transactional;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import ma.enset.utilisateur.constant.CoreConstants;
//import ma.enset.utilisateur.exception.ElementAlreadyExistsException;
//import ma.enset.utilisateur.exception.ElementNotFoundException;
//import ma.enset.utilisateur.exception.InternalErrorException;
//import ma.enset.utilisateur.model.Permission;
//import ma.enset.utilisateur.model.Role;
//import ma.enset.utilisateur.repository.RoleRepository;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//@AllArgsConstructor
//@Slf4j
//public class RoleServiceImpl implements RoleService {
//    private final RoleRepository roleRepository;
//
//    private final PermissionService permissionService;
//
//    @Override
//    public Role save(Role role) throws ElementAlreadyExistsException {
//        if (roleRepository.existsByRoleId(role.getRoleId()))
//            throw ElementAlreadyExistsException.builder()
//                    .key(CoreConstants.BusinessExceptionMessage.ROLE_ALREADY_EXISTS)
//                    .args(new Object[]{role.getRoleId()})
//                    .build();
//
//        checkPermissions(role);
//
//        Role savedRole = null;
//
//        try {
//            savedRole = roleRepository.save(role);
//        } catch (Exception e) {
//            log.error(e.getMessage(), e.getCause());
//            throw new InternalErrorException();
//        }
//
//        return savedRole;
//    }
//
//
//    @Transactional
//    @Override
//    public List<Role> saveAll(List<Role> roles) throws ElementAlreadyExistsException, InternalErrorException {
//        List<Role> savedRoles = new ArrayList<>();
//        roles.forEach(role -> savedRoles.add(save(role)));
//        return savedRoles;
//    }
//
//    @Override
//    public Page<Role> findAll(Pageable pageable) {
//        return roleRepository.findAll(pageable);
//    }
//
//
//    @Override
//    public Role update(Role role) throws ElementNotFoundException, InternalErrorException {
//
//        Role roleToUpdate = roleRepository.findByRoleId(role.getRoleId()).orElseThrow(
//                () -> roleNotFoundException(role.getRoleId())
//        );
//
//        checkPermissions(roleToUpdate);
//
//        Role updatedRole = null;
//
//        try {
//            updatedRole = roleRepository.save(roleToUpdate);
//        } catch (Exception e) {
//            log.error(e.getMessage(), e.getCause());
//            throw new InternalErrorException();
//        }
//
//        return updatedRole;
//    }
//
//
//    @Transactional
//    @Override
//    public List<Role> updateAll(List<Role> roles) throws ElementNotFoundException, InternalErrorException {
//
//        List<Role> updatedRoles = new ArrayList<>();
//
//        roles.forEach(role -> updatedRoles.add(update(role)));
//
//        return updatedRoles;
//    }
//
//    @Override
//    public void deleteByRoleId(String codeRole) throws ElementNotFoundException {
//
//        if (!roleRepository.existsByRoleId(codeRole))
//            throw roleNotFoundException(codeRole);
//
//        roleRepository.deleteByRoleId(codeRole);
//    }
//
//    @Transactional
//    @Override
//    public void deleteAllByRoleId(List<String> codeRoles) throws ElementNotFoundException {
//        codeRoles.forEach(this::deleteByRoleId);
//    }
//
//    @Override
//    public Role findByRoleId(String codeRole) throws ElementNotFoundException {
//        return roleRepository.findByRoleId(codeRole)
//                .orElseThrow(() ->
//                        roleNotFoundException(codeRole)
//                );
//    }
//
//    @Override
//    public List<Role> findAllByRoleId(List<String> codeRoles) throws ElementNotFoundException {
//        List<Role> roles = new ArrayList<>();
//        codeRoles.forEach(codeRole -> roles.add(findByRoleId(codeRole)));
//        return roles;
//    }
//
//
//    private void checkPermissions(Role role) {
//
//        if (role.getPermissions() == null) return;
//
//        List<Integer> permissions = role.getPermissions()
//                .stream()
//                .map(Permission::getPermissionId)
//                .toList();
//
//        permissionService.findAllByPermissionId(permissions);
//    }
//
//
//    private Role extractNullValues(Role role, Role roleToUpdate) {
//        if (role.getRoleName() != null)
//            roleToUpdate.setRoleName(role.getRoleName());
//        if (role.getPermissions() != null)
//            roleToUpdate.setPermissions(role.getPermissions());
//        return roleToUpdate;
//    }
//
//    private ElementNotFoundException roleNotFoundException(String codeRole) {
//        return ElementNotFoundException.builder()
//                .key(CoreConstants.BusinessExceptionMessage.ROLE_NOT_FOUND)
//                .args(new Object[]{codeRole})
//                .build();
//    }
//
//}
