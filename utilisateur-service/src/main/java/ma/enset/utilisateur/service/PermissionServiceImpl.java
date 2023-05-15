package ma.enset.utilisateur.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.utilisateur.constant.CoreConstants;
import ma.enset.utilisateur.dto.PagingResponse;
import ma.enset.utilisateur.dto.permission.PermissionCreateRequest;
import ma.enset.utilisateur.dto.permission.PermissionResponse;
import ma.enset.utilisateur.dto.permission.PermissionUpdateRequest;
import ma.enset.utilisateur.exception.DuplicateEntryException;
import ma.enset.utilisateur.exception.ElementAlreadyExistsException;
import ma.enset.utilisateur.exception.ElementNotFoundException;
import ma.enset.utilisateur.mapper.PermissionMapper;
import ma.enset.utilisateur.model.Permission;
import ma.enset.utilisateur.repository.PermissionRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class PermissionServiceImpl implements PermissionService {
    private final String ELEMENT_TYPE = "Permission";
    private final String ID_FIELD_NAME = "permissionId";
    private final PermissionMapper permissionMapper;
    private final PermissionRepository permissionRepository;

    @Override
    public PermissionResponse save(PermissionCreateRequest permissionCreateRequest) throws ElementAlreadyExistsException {

        Permission permission = permissionMapper.toPermission(permissionCreateRequest);

        Permission savedPermission = null;
        if (permissionRepository.existsById(permission.getPermissionId()))
            throw new ElementAlreadyExistsException(
                    CoreConstants.BusinessExceptionMessage.ALREADY_EXISTS,
                    new Object[]{ELEMENT_TYPE, ID_FIELD_NAME, permission.getPermissionId()},
                    null
            );

        try {
            savedPermission = permissionRepository.save(permission);
        } catch (DataIntegrityViolationException e) {
            throw new ElementAlreadyExistsException(
                    CoreConstants.BusinessExceptionMessage.ALREADY_EXISTS,
                    new Object[]{ELEMENT_TYPE, ID_FIELD_NAME, permission.getPermissionId()},
                    null
            );
        }

        return permissionMapper.toPermissionResponse(savedPermission);
    }

    @Override
    @Transactional
    public List<PermissionResponse> saveAll(List<PermissionCreateRequest> permissionCreateRequestList) throws ElementAlreadyExistsException {
        List<Permission> permissionList = permissionMapper.toPermissionList(permissionCreateRequestList);
        System.out.println(permissionList.size());
        List<PermissionResponse> createdPermissionList;
        try {
            createdPermissionList = permissionMapper.toPermissionResponseList(permissionRepository.saveAll(permissionList));
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEntryException(
                    CoreConstants.BusinessExceptionMessage.DUPLICATE_ENTRY,
                    null
            );
        }
        return createdPermissionList;
    }

    @Override
    public PermissionResponse findById(int id) throws ElementNotFoundException {
        return permissionMapper.toPermissionResponse(permissionRepository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException(
                        CoreConstants.BusinessExceptionMessage.NOT_FOUND,
                        new Object[]{ELEMENT_TYPE, ID_FIELD_NAME, id},
                        null
                )));
    }

    @Override
    public List<PermissionResponse> findAllById(Set<Integer> ids) throws ElementNotFoundException {

        final List<Permission> permissionList = permissionRepository.findAllById(ids);
        final List<Integer> foundIds = permissionList.stream()
                .map(Permission::getPermissionId)
                .toList();
        if (permissionList.size() != ids.size())
            throw new ElementNotFoundException(
                    CoreConstants.BusinessExceptionMessage.MANY_NOT_FOUND,
                    new Object[]{ELEMENT_TYPE},
                    ids.stream()
                            .filter(id -> !foundIds.contains(id))
                            .map(String::valueOf)
                            .toList()
            );

        return permissionMapper.toPermissionResponseList(permissionList);
    }

    @Override
    public PagingResponse<PermissionResponse> findAll(int page, int size) {
        return permissionMapper.toPagingResponse(permissionRepository.findAll(PageRequest.of(page, size)));
    }

    @Override
    public PermissionResponse update(int id, PermissionUpdateRequest permissionUpdateRequest) throws ElementNotFoundException {

        Permission toBeUpdatedPermission = permissionRepository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException(
                        CoreConstants.BusinessExceptionMessage.NOT_FOUND,
                        new Object[]{ELEMENT_TYPE, ID_FIELD_NAME, id},
                        null
                ));

        permissionMapper.updateRequestToPermission(permissionUpdateRequest, toBeUpdatedPermission);
        toBeUpdatedPermission.setPermissionId(id);

        return permissionMapper.toPermissionResponse(permissionRepository.save(toBeUpdatedPermission));
    }

    @Override
    @Transactional
    public List<PermissionResponse> updateAll(List<PermissionUpdateRequest> permissionUpdateRequestList) throws ElementNotFoundException {

        Set<Integer> ids = permissionUpdateRequestList.stream()
                .map(PermissionUpdateRequest::permissionId)
                .collect(Collectors.toSet());

        final List<Permission> permissionList = permissionRepository.findAllById(ids);

        Set<Integer> foundIds = permissionList.stream()
                .map(Permission::getPermissionId)
                .collect(Collectors.toSet());

        if (foundIds.size() != ids.size())
            throw new ElementNotFoundException(
                    CoreConstants.BusinessExceptionMessage.MANY_NOT_FOUND,
                    new Object[]{ELEMENT_TYPE},
                    ids.stream()
                            .filter(id -> !foundIds.contains(id))
                            .map(String::valueOf)
                            .toList()
            );

        permissionMapper.updateRequestsToPermissions(permissionUpdateRequestList, permissionList);

        return permissionMapper.toPermissionResponseList(
                permissionRepository.saveAll(permissionList)
        );
    }

    @Override
    public boolean exists(Set<Integer> permissionIdList) {

        Set<Integer> foundIds = permissionRepository.findAllById(permissionIdList)
                .stream()
                .map(Permission::getPermissionId)
                .collect(Collectors.toSet());

        if (foundIds.size() != permissionIdList.size())
            throw new ElementNotFoundException(
                    CoreConstants.BusinessExceptionMessage.MANY_NOT_FOUND,
                    new Object[]{ELEMENT_TYPE},
                    permissionIdList.stream()
                            .filter(id -> !foundIds.contains(id))
                            .map(String::valueOf)
                            .toList()
            );

        return true;
    }

    @Override
    public void deleteById(int permissionId) throws ElementNotFoundException {
        if (!permissionRepository.existsById(permissionId))
            throw new ElementNotFoundException(
                    CoreConstants.BusinessExceptionMessage.NOT_FOUND,
                    new Object[]{ELEMENT_TYPE, ID_FIELD_NAME, permissionId},
                    null
            );
        permissionRepository.deleteById(permissionId);
    }

    @Override
    @Transactional
    public void deleteAllById(Set<Integer> permissionIds) throws ElementNotFoundException {
        this.exists(permissionIds);
        permissionRepository.deleteAllById(permissionIds);
    }
}
