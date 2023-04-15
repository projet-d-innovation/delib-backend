package ma.enset.utilisateur.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.utilisateur.constant.CoreConstants;
import ma.enset.utilisateur.exception.ElementAlreadyExistsException;
import ma.enset.utilisateur.exception.ElementNotFoundException;
import ma.enset.utilisateur.exception.InternalErrorException;
import ma.enset.utilisateur.model.Permission;
import ma.enset.utilisateur.repository.PermissionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class PermissionServiceImpl implements PermissionService {
    private final PermissionRepository permissionRepository;

    @Override
    public Permission save(Permission permission) throws ElementAlreadyExistsException {
        Permission savedPermission = null;
        if (permissionRepository.existsByPermissionId(permission.getPermissionId()))
            throw ElementAlreadyExistsException.builder()
                    .key(CoreConstants.BusinessExceptionMessage.PERMISSION_ALREADY_EXISTS)
                    .args(new Object[]{permission.getPermissionId()})
                    .build();

        try {
            savedPermission = permissionRepository.save(permission);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            throw new InternalErrorException();
        }

        return savedPermission;
    }

    @Transactional
    @Override
    public List<Permission> saveAll(List<Permission> permissions) throws ElementAlreadyExistsException {
        List<Permission> savedPermissions = new ArrayList<>();
        permissions.forEach(permission -> savedPermissions.add(save(permission)));
        return savedPermissions;
    }

    @Override
    public Page<Permission> findAll(Pageable pageable) {
        return permissionRepository.findAll(pageable);
    }


    @Override
    public Permission update(Permission permission) throws ElementNotFoundException {

        log.debug("Permission to be updated: " + permission.getPermissionId());
        if (!permissionRepository.existsByPermissionId(permission.getPermissionId())) {
            throw permissionNotFoundException(String.valueOf(permission.getPermissionId()));
        }

        Permission updatedPermission = null;

        try {
            updatedPermission = permissionRepository.save(permission);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            throw new InternalErrorException();
        }

        return updatedPermission;
    }

    @Transactional
    @Override
    public List<Permission> updateAll(List<Permission> permissions) throws ElementNotFoundException {
        List<Permission> updatedPermissions = new ArrayList<>();
        permissions.forEach(permission -> updatedPermissions.add(update(permission)));
        return updatedPermissions;
    }

    @Override
    public void deleteByPermissionId(int permissionId) throws ElementNotFoundException {

        Permission toBeDeleted = permissionRepository.findByPermissionId(permissionId).orElse(null);

        if (toBeDeleted == null) throw permissionNotFoundException(String.valueOf(permissionId));

        permissionRepository.deleteByPermissionId(permissionId);
    }

    @Transactional
    @Override
    public void deleteAllByPermissionId(List<Integer> permissionIds) throws ElementNotFoundException {
        permissionIds.forEach(this::deleteByPermissionId);
    }

    @Override
    public Permission findByPermissionId(int permissionId) throws ElementNotFoundException {
        log.debug("Permission to be found: " + permissionId);
        return permissionRepository.findByPermissionId(permissionId)
                .orElseThrow(() ->
                        permissionNotFoundException(String.valueOf(permissionId))
                );
    }

    @Override
    public List<Permission> findAllByPermissionId(List<Integer> permissionIds) throws ElementNotFoundException {
        List<Permission> permissions = new ArrayList<>();
        permissionIds.forEach(permissionId -> permissions.add(findByPermissionId(permissionId)));
        return permissions;
    }

    private ElementNotFoundException permissionNotFoundException(String codePermission) {
        return ElementNotFoundException.builder()
                .key(CoreConstants.BusinessExceptionMessage.PERMISSION_NOT_FOUND)
                .args(new Object[]{codePermission})
                .build();
    }

}
