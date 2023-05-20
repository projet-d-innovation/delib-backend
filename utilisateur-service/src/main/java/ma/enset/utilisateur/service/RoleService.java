package ma.enset.utilisateur.service;

import ma.enset.utilisateur.dto.PagingResponse;
import ma.enset.utilisateur.dto.role.RoleCreateRequest;
import ma.enset.utilisateur.dto.role.RoleResponse;
import ma.enset.utilisateur.dto.role.RoleUpdateRequest;
import ma.enset.utilisateur.exception.ElementAlreadyExistsException;
import ma.enset.utilisateur.exception.ElementNotFoundException;

import java.util.List;
import java.util.Set;

public interface RoleService {
    RoleResponse save(RoleCreateRequest roleCreateRequest) throws ElementAlreadyExistsException;

    List<RoleResponse> saveAll(List<RoleCreateRequest> roleCreateRequests) throws ElementAlreadyExistsException;

    RoleResponse findById(String id, boolean includePermissions) throws ElementNotFoundException;

    List<RoleResponse> findAllById(Set<String> ids, boolean includePermissions) throws ElementNotFoundException;

    PagingResponse<RoleResponse> findAll(int page, int size, boolean includePermissions);

    RoleResponse update(String id, RoleUpdateRequest roleUpdateRequest) throws ElementNotFoundException;

    List<RoleResponse> updateAll(List<RoleUpdateRequest> roleUpdateRequests) throws ElementNotFoundException;

    boolean exists(Set<String> roleIdList);

    void deleteById(String id) throws ElementNotFoundException;

    void deleteAllById(Set<String> ids) throws ElementNotFoundException;

}
