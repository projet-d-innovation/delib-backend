package ma.enset.utilisateur.service;

import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import ma.enset.utilisateur.constant.CoreConstants;
import ma.enset.utilisateur.exception.PermissionAlreadyExistsException;
import ma.enset.utilisateur.exception.PermissionNotFoundException;
import ma.enset.utilisateur.model.Permission;
import ma.enset.utilisateur.repository.PermissionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class PermissionServiceImpl implements PermissionService {
    private final PermissionRepository permissionRepository;

    @Override
    public Permission create(Permission permission) throws PermissionAlreadyExistsException {
        Permission createdPermission = null;
        if (permissionRepository.existsById(permission.getPermissionId()))
            throw PermissionAlreadyExistsException.builder()
                    .key(CoreConstants.BusinessExceptionMessage.PERMISSION_ALREADY_EXISTS)
                    .args(new Object[]{"code", permission.getPermissionId()})
                    .build();

        try {
            createdPermission = permissionRepository.save(permission);
        } catch (Exception e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                throw PermissionAlreadyExistsException.builder()
                        .key(CoreConstants.BusinessExceptionMessage.INTERNAL_ERROR)
                        .args(new Object[]{"code", permission.getPermissionId()})
                        .build();
            }
        }

        return createdPermission;
    }

    @Override
    public List<Permission> createMany(List<Permission> permissions) throws PermissionAlreadyExistsException {
        List<Permission> createdPermissions = new ArrayList<>();
        for (Permission permission : permissions) {
            if (permissionRepository.existsById(permission.getPermissionId()))
                throw PermissionAlreadyExistsException.builder()
                        .key(CoreConstants.BusinessExceptionMessage.PERMISSION_ALREADY_EXISTS)
                        .args(new Object[]{"code", permission.getPermissionId()})
                        .build();
        }

        try {
            createdPermissions = permissionRepository.saveAll(permissions);
        } catch (Exception e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                throw PermissionAlreadyExistsException.builder()
                        .key(CoreConstants.BusinessExceptionMessage.INTERNAL_ERROR)
                        .build();
            }
        }

        return createdPermissions;
    }

    @Override
    public Page<Permission> findAll(Pageable pageable) {
        return permissionRepository.findAll(pageable);
    }


    @Override
    public Permission update(Permission permission) throws PermissionNotFoundException {

        if (!permissionRepository.existsById(permission.getPermissionId())) {
            throw PermissionNotFoundException.builder()
                    .key(CoreConstants.BusinessExceptionMessage.PERMISSION_NOT_FOUND)
                    .args(new Object[]{"code", permission.getPermissionId()})
                    .build();
        }

        Permission updatedPermission = null;

        try {
            updatedPermission = permissionRepository.save(permission);
        } catch (Exception e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                throw PermissionAlreadyExistsException.builder()
                        .key(CoreConstants.BusinessExceptionMessage.PERMISSION_ALREADY_EXISTS)
                        .args(new Object[]{"code", permission.getPermissionId()})
                        .build();
            }
        }

        return updatedPermission;
    }

    @Override
    public void deleteById(int permissionId) throws PermissionNotFoundException {

        Permission toBeDeleted = permissionRepository.findById(permissionId).orElse(null);

        if (toBeDeleted == null)
            throw PermissionNotFoundException.builder()
                    .key(CoreConstants.BusinessExceptionMessage.PERMISSION_NOT_FOUND)
                    .args(new Object[]{"code", permissionId})
                    .build();

        permissionRepository.deleteById(permissionId);
    }

    @Override
    public void deleteManyById(List<Integer> permissionIds) throws PermissionNotFoundException {
        for (int permissionId : permissionIds) {
            this.deleteById(permissionId);
        }
    }

    @Override
    public Permission findById(int permissionId) throws PermissionNotFoundException {
        return permissionRepository.findById(permissionId)
                .orElseThrow(() ->
                        PermissionNotFoundException.builder()
                                .key(CoreConstants.BusinessExceptionMessage.PERMISSION_NOT_FOUND)
                                .args(new Object[]{"code", permissionId})
                                .build()
                );
    }

    @Override
    public List<Permission> findManyById(List<Integer> permissionIds) throws PermissionNotFoundException {
        List<Permission> permissions = new ArrayList<>();
        for (int permissionId : permissionIds) {
            permissions.add(findById(permissionId));
        }
        return permissions;
    }


}
