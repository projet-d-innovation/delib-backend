package ma.enset.utilisateur.service;

import ma.enset.utilisateur.exception.PermissionAlreadyExistsException;
import ma.enset.utilisateur.exception.PermissionNotFoundException;
import ma.enset.utilisateur.model.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PermissionService {
    Permission create(Permission permission) throws PermissionAlreadyExistsException;

    List<Permission> createMany(List<Permission> permissions) throws PermissionAlreadyExistsException;

    Permission findById(int permissionId) throws PermissionNotFoundException;

    List<Permission> findManyById(List<Integer> permissionIds) throws PermissionNotFoundException;

    Page<Permission> findAll(Pageable pageable);

    Permission update(Permission permission) throws PermissionNotFoundException;

    void deleteById(int permissionId) throws PermissionNotFoundException;

    void deleteManyById(List<Integer> permissionIds) throws PermissionNotFoundException;

}
