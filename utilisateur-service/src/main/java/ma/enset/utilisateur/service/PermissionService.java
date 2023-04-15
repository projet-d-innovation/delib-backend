package ma.enset.utilisateur.service;

import ma.enset.utilisateur.exception.ElementAlreadyExistsException;
import ma.enset.utilisateur.exception.ElementNotFoundException;
import ma.enset.utilisateur.model.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PermissionService {
    Permission save(Permission permission) throws ElementAlreadyExistsException;

    List<Permission> saveAll(List<Permission> permissions) throws ElementAlreadyExistsException;

    Permission findByPermissionId(int permissionId) throws ElementNotFoundException;

    List<Permission> findAllByPermissionId(List<Integer> permissionIds) throws ElementNotFoundException;

    Page<Permission> findAll(Pageable pageable);

    Permission update(Permission permission) throws ElementNotFoundException;
    List<Permission> updateAll(List<Permission> permission) throws ElementNotFoundException;

    void deleteByPermissionId(int permissionId) throws ElementNotFoundException;

    void deleteAllByPermissionId(List<Integer> permissionIds) throws ElementNotFoundException;

}
