package ma.enset.utilisateur.service;

import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import ma.enset.utilisateur.constant.CoreConstants;
import ma.enset.utilisateur.exception.InternalErrorException;
import ma.enset.utilisateur.exception.RoleAlreadyExistsException;
import ma.enset.utilisateur.exception.RoleNotFoundException;
import ma.enset.utilisateur.model.Role;
import ma.enset.utilisateur.repository.RoleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Role create(Role role) throws RoleAlreadyExistsException {
        Role createdRole = null;
        if (roleRepository.existsById(role.getRoleId()))
            throw RoleAlreadyExistsException.builder()
                    .key(CoreConstants.BusinessExceptionMessage.ROLE_ALREADY_EXISTS)
                    .args(new Object[]{"code", role.getRoleId()})
                    .build();

        try {
            createdRole = roleRepository.save(role);
        } catch (Exception e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                throw RoleAlreadyExistsException.builder()
                        .key(CoreConstants.BusinessExceptionMessage.INTERNAL_ERROR)
                        .args(new Object[]{"code", role.getRoleId()})
                        .build();
            }
        }

        return createdRole;
    }

    @Override
    public List<Role> createMany(List<Role> roles) throws RoleAlreadyExistsException, InternalErrorException {
        List<Role> createdRoles = new ArrayList<>();
        for (Role role : roles) {
            if (roleRepository.existsById(role.getRoleId()))
                throw RoleAlreadyExistsException.builder()
                        .key(CoreConstants.BusinessExceptionMessage.ROLE_ALREADY_EXISTS)
                        .args(new Object[]{"code", role.getRoleId()})
                        .build();
        }

        try {
            createdRoles = roleRepository.saveAll(roles);
        } catch (Exception e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                throw InternalErrorException.builder()
                        .key(CoreConstants.BusinessExceptionMessage.INTERNAL_ERROR)
                        .build();
            }
        }

        return createdRoles;
    }

    @Override
    public Page<Role> findAll(Pageable pageable) {
        return roleRepository.findAll(pageable);
    }


    @Override
    public Role update(Role role) throws RoleNotFoundException, InternalErrorException {

        Role roleToUpdate = roleRepository.findById(role.getRoleId()).orElseThrow(
                () -> RoleNotFoundException.builder()
                        .key(CoreConstants.BusinessExceptionMessage.ROLE_NOT_FOUND)
                        .args(new Object[]{"code", role.getRoleId()})
                        .build()
        );

        roleToUpdate = extractNullValues(role, roleToUpdate);

        Role updatedRole = null;

        try {
            updatedRole = roleRepository.save(roleToUpdate);
        } catch (Exception e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                throw InternalErrorException.builder()
                        .key(CoreConstants.BusinessExceptionMessage.ROLE_ALREADY_EXISTS)
                        .args(new Object[]{"code", role.getRoleId()})
                        .build();
            }
        }

        return updatedRole;
    }


    @Override
    public List<Role> updateMany(List<Role> roles) throws RoleNotFoundException, InternalErrorException {

        List<Role> updatedRoles = new ArrayList<>();

        for (Role role : roles) {
            updatedRoles.add(this.update(role));
        }

        return updatedRoles;
    }

    @Override
    public void deleteById(String codeRole) throws RoleNotFoundException {

        Role toBeDeleted = roleRepository.findById(codeRole).orElse(null);

        if (toBeDeleted == null)
            throw RoleNotFoundException.builder()
                    .key(CoreConstants.BusinessExceptionMessage.ROLE_NOT_FOUND)
                    .args(new Object[]{"code", codeRole})
                    .build();

        roleRepository.deleteById(codeRole);
    }

    @Override
    public void deleteManyById(List<String> codeRoles) throws RoleNotFoundException {
        for (String codeRole : codeRoles) {
            this.deleteById(codeRole);
        }
    }

    @Override
    public Role findById(String codeRole) throws RoleNotFoundException {
        return roleRepository.findById(codeRole)
                .orElseThrow(() ->
                        RoleNotFoundException.builder()
                                .key(CoreConstants.BusinessExceptionMessage.ROLE_NOT_FOUND)
                                .args(new Object[]{"code", codeRole})
                                .build()
                );
    }

    @Override
    public List<Role> findManyById(List<String> codeRoles) throws RoleNotFoundException {
        List<Role> roles = new ArrayList<>();
        for (String codeRole : codeRoles) {
            roles.add(findById(codeRole));
        }
        return roles;
    }


    private Role extractNullValues(Role role, Role roleToUpdate) {
        if (role.getRoleName() != null)
            roleToUpdate.setRoleName(role.getRoleName());
        if (role.getPermissions() != null)
            roleToUpdate.setPermissions(role.getPermissions());
        return roleToUpdate;
    }

}
