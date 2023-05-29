package ma.enset.utilisateur.service;

import ma.enset.utilisateur.dto.IncludeParams;
import ma.enset.utilisateur.dto.PagingResponse;
import ma.enset.utilisateur.dto.utilisateur.UtilisateurCreateRequest;
import ma.enset.utilisateur.dto.utilisateur.UtilisateurResponse;
import ma.enset.utilisateur.dto.utilisateur.UtilisateurUpdateRequest;
import ma.enset.utilisateur.exception.*;

import java.util.List;
import java.util.Set;

public interface UtilisateurService {

    UtilisateurResponse save(UtilisateurCreateRequest request) throws ElementAlreadyExistsException;

    List<UtilisateurResponse> saveAll(List<UtilisateurCreateRequest> requestList) throws ElementAlreadyExistsException, DuplicateEntryException;

    UtilisateurResponse findById(String codeUtilisateur, IncludeParams includes) throws ElementNotFoundException;

    List<UtilisateurResponse> findAllById(Set<String> codeList, IncludeParams includes) throws ElementNotFoundException;

    PagingResponse<UtilisateurResponse> findAll(
            int page, int size, String search,
            IncludeParams includes
    );

    PagingResponse<UtilisateurResponse> findAllByGroup(
            int page, int size,
            String search, String roleGroup,
            IncludeParams includes
    );

    PagingResponse<UtilisateurResponse> findAllByRole(
            int page, int size,
            String search, String roleId,
            IncludeParams includes
    );

    UtilisateurResponse update(String code, UtilisateurUpdateRequest request) throws ElementNotFoundException;

    List<UtilisateurResponse> updateAll(List<UtilisateurUpdateRequest> requestList) throws ElementNotFoundException, DuplicateEntryException;

    void deleteById(String code) throws ElementNotFoundException;

    void deleteAllById(Set<String> codeList) throws ElementNotFoundException;

    boolean exists(Set<String> codeList);

    void handleKeyDepartementDeletion(Set<String> codeDepartement);
}
