package ma.enset.utilisateur.service;

import ma.enset.utilisateur.dto.PagingResponse;
import ma.enset.utilisateur.dto.permission.PermissionCreateRequest;
import ma.enset.utilisateur.dto.permission.PermissionResponse;
import ma.enset.utilisateur.dto.permission.PermissionUpdateRequest;
import ma.enset.utilisateur.exception.ElementAlreadyExistsException;
import ma.enset.utilisateur.exception.ElementNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface PermissionService {
    PermissionResponse save(PermissionCreateRequest permissionCreateRequest) throws ElementAlreadyExistsException;

    List<PermissionResponse> saveAll(List<PermissionCreateRequest> permissionCreateRequestList) throws ElementAlreadyExistsException;

    PermissionResponse findById(int id) throws ElementNotFoundException;

    List<PermissionResponse> findAllById(Set<Integer> ids) throws ElementNotFoundException;

    PagingResponse<PermissionResponse> findAll(int page, int size);

    PermissionResponse update(int id, PermissionUpdateRequest permissionUpdateRequest) throws ElementNotFoundException;

    List<PermissionResponse> updateAll(List<PermissionUpdateRequest> permissionUpdateRequestList) throws ElementNotFoundException;

    boolean exists(Set<Integer> permissionIdList);

    void deleteById(int permissionId) throws ElementNotFoundException;

    void deleteAllById(Set<Integer> permissionIds) throws ElementNotFoundException;

}
