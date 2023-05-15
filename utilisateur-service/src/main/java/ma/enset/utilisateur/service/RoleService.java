package ma.enset.utilisateur.service;

import ma.enset.utilisateur.dto.role.RoleCreateRequest;
import ma.enset.utilisateur.dto.role.RoleResponse;
import ma.enset.utilisateur.dto.role.RoleUpdateRequest;
import ma.enset.utilisateur.exception.InternalErrorException;
import ma.enset.utilisateur.exception.ElementAlreadyExistsException;
import ma.enset.utilisateur.exception.ElementNotFoundException;
import ma.enset.utilisateur.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoleService {
    RoleResponse save(RoleCreateRequest roleCreateRequest) throws ElementAlreadyExistsException;

    List<RoleResponse> saveAll(List<RoleCreateRequest> roleCreateRequests) throws ElementAlreadyExistsException;

    RoleResponse findById(String id) throws ElementNotFoundException;

    List<RoleResponse> findAllById(List<String> ids) throws ElementNotFoundException;

    Page<RoleResponse> findAll(Pageable pageable);

    RoleResponse update(RoleUpdateRequest roleUpdateRequest) throws ElementNotFoundException;

    List<RoleResponse> updateAll(List<RoleUpdateRequest> roleUpdateRequests) throws ElementNotFoundException;

    boolean exists(List<String> roleIdList);

    void deleteById(String id) throws ElementNotFoundException;

    void deleteAllById(List<String> ids) throws ElementNotFoundException;

}
