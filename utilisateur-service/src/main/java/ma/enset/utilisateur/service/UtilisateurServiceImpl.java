package ma.enset.utilisateur.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.utilisateur.client.DepartementClient;
import ma.enset.utilisateur.client.ElementClient;
import ma.enset.utilisateur.client.FiliereClient;
import ma.enset.utilisateur.constant.CoreConstants;
import ma.enset.utilisateur.dto.*;
import ma.enset.utilisateur.dto.permission.PermissionResponse;
import ma.enset.utilisateur.dto.utilisateur.UtilisateurCreateRequest;
import ma.enset.utilisateur.dto.utilisateur.UtilisateurResponse;
import ma.enset.utilisateur.dto.utilisateur.UtilisateurUpdateRequest;
import ma.enset.utilisateur.exception.*;
import ma.enset.utilisateur.mapper.UtilisateurMapper;
import ma.enset.utilisateur.model.Role;
import ma.enset.utilisateur.model.Utilisateur;
import ma.enset.utilisateur.repository.UtilisateurRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class UtilisateurServiceImpl implements UtilisateurService {
    private final String ELEMENT_TYPE = "Utilisateur";
    private final String ID_FIELD_NAME = "code";
    private final UtilisateurRepository utilisateurRepository;
    private final UtilisateurMapper utilisateurMapper;

    private final RoleService roleService;

    private final ElementClient elementClient;
    private final DepartementClient departementClient;
    private final FiliereClient filiereClient;

    @Override
    public UtilisateurResponse save(UtilisateurCreateRequest request) throws ElementAlreadyExistsException {
        if (utilisateurRepository.existsById(request.code())) {
            throw new ElementAlreadyExistsException(
                    CoreConstants.BusinessExceptionMessage.ALREADY_EXISTS,
                    new Object[]{ELEMENT_TYPE, ID_FIELD_NAME, request.code()},
                    null
            );
        }

        Utilisateur toBeCreateedUtilisateur = utilisateurMapper.toUtilisateur(request);

        checkForeignKeys(toBeCreateedUtilisateur);

        Utilisateur utilisateur;
        try {
            utilisateur = utilisateurRepository.save(toBeCreateedUtilisateur);
        } catch (DataIntegrityViolationException e) {
            throw new ElementAlreadyExistsException(
                    CoreConstants.BusinessExceptionMessage.ALREADY_EXISTS,
                    new Object[]{ELEMENT_TYPE, ID_FIELD_NAME, toBeCreateedUtilisateur.getCode()},
                    null
            );
        }

        return utilisateurMapper.toUtilisateurResponse(utilisateur);
    }

    @Transactional
    @Override
    public List<UtilisateurResponse> saveAll(List<UtilisateurCreateRequest> requestList) throws ElementAlreadyExistsException, DuplicateEntryException {

        Set<String> codes = requestList.stream()
                .map(UtilisateurCreateRequest::code)
                .collect(Collectors.toSet());

        if (codes.size() != requestList.size()) {
            throw new DuplicateEntryException(
                    CoreConstants.BusinessExceptionMessage.DUPLICATE_ENTRY,
                    new Object[]{ELEMENT_TYPE, ID_FIELD_NAME}
            );
        }

        List<Utilisateur> foundUtilisateurs = utilisateurRepository.findAllById(codes);

        if (!foundUtilisateurs.isEmpty()) {
            throw new ElementAlreadyExistsException(
                    CoreConstants.BusinessExceptionMessage.MANY_ALREADY_EXISTS,
                    new Object[]{ELEMENT_TYPE},
                    foundUtilisateurs.stream()
                            .map(Utilisateur::getCode)
                            .collect(Collectors.toList())
            );
        }


        List<Utilisateur> toBeCreatedUtilisateurs = utilisateurMapper.toUtilisateurList(requestList);

        checkForeignKeys(toBeCreatedUtilisateurs);

        return utilisateurMapper.toUtilisateurResponseList(
                utilisateurRepository.saveAll(toBeCreatedUtilisateurs)
        );
    }

    @Override
    public UtilisateurResponse findById(String codeUtilisateur, IncludeParams includes) throws ElementNotFoundException {

        UtilisateurResponse utilisateur = utilisateurMapper.toUtilisateurResponse(
                utilisateurRepository.findById(codeUtilisateur)
                        .orElseThrow(() -> new ElementNotFoundException(
                                CoreConstants.BusinessExceptionMessage.NOT_FOUND,
                                new Object[]{ELEMENT_TYPE, ID_FIELD_NAME, codeUtilisateur},
                                null
                        ))
        );

        handleNested(utilisateur, includes);

        return utilisateur;
    }

    @Override
    public List<UtilisateurResponse> findAllById(Set<String> codeList, IncludeParams includes) throws ElementNotFoundException {

        final List<Utilisateur> foundUtilisateurs = utilisateurRepository.findAllById(codeList);
        final List<String> codes = foundUtilisateurs.stream().map(Utilisateur::getCode).toList();

        if (codes.size() != codeList.size()) {
            throw new ElementNotFoundException(
                    CoreConstants.BusinessExceptionMessage.MANY_NOT_FOUND,
                    new Object[]{ELEMENT_TYPE},
                    codeList.stream()
                            .filter(code -> !codes.contains(code))
                            .collect(Collectors.toList())
            );
        }

        List<UtilisateurResponse> utilisateurs = utilisateurMapper.toUtilisateurResponseList(foundUtilisateurs);
        handleNested(utilisateurs, includes);
        return utilisateurs;
    }

    @Override
    public PagingResponse<UtilisateurResponse> findAll(int page,
                                                       int size,
                                                       String search,
                                                       IncludeParams includes) {

        PagingResponse<UtilisateurResponse> utilisateurs = utilisateurMapper.toPagingResponse(
                utilisateurRepository.findAll(PageRequest.of(page, size))
        );

        handleNested(utilisateurs.records(), includes);

        return utilisateurs;
    }

    @Override
    public PagingResponse<UtilisateurResponse> findAllByGroup(int page,
                                                              int size,
                                                              String search,
                                                              String roleGroup,
                                                              IncludeParams includes) {

        PagingResponse<UtilisateurResponse> utilisateurs = utilisateurMapper.toPagingResponse(
                utilisateurRepository.findAllByRoles_Groupe_AndNomContainsIgnoreCase(
                        roleGroup, search, PageRequest.of(page, size)
                )
        );

        handleNested(utilisateurs.records(), includes);

        return utilisateurs;
    }

    @Override
    public PagingResponse<UtilisateurResponse> findAllByRole(int page,
                                                             int size,
                                                             String search,
                                                             String roleId,
                                                             IncludeParams includes) {
        PagingResponse<UtilisateurResponse> utilisateurs = utilisateurMapper.toPagingResponse(
                utilisateurRepository.findAllByRoles_RoleId_AndNomContainsIgnoreCase(
                        roleId, search, PageRequest.of(page, size)
                )
        );

        handleNested(utilisateurs.records(), includes);

        return utilisateurs;
    }

    @Override
    public UtilisateurResponse update(String code, UtilisateurUpdateRequest request) throws ElementNotFoundException {

        Utilisateur toBeUpdated = utilisateurRepository.findById(code)
                .orElseThrow(() -> new ElementNotFoundException(
                        CoreConstants.BusinessExceptionMessage.NOT_FOUND,
                        new Object[]{ELEMENT_TYPE, ID_FIELD_NAME, code},
                        null
                ));

        utilisateurMapper.updateRequestToUtilisateur(request, toBeUpdated);
        toBeUpdated.setCode(code);

        checkForeignKeys(toBeUpdated);

        return utilisateurMapper.toUtilisateurResponse(
                utilisateurRepository.save(toBeUpdated)
        );
    }

    @Transactional
    @Override
    public List<UtilisateurResponse> updateAll(List<UtilisateurUpdateRequest> requestList) throws ElementNotFoundException, DuplicateEntryException {

        Set<String> codes = requestList.stream()
                .map(UtilisateurUpdateRequest::code)
                .collect(Collectors.toSet());

        if (codes.size() != requestList.size()) {
            throw new DuplicateEntryException(
                    CoreConstants.BusinessExceptionMessage.DUPLICATE_ENTRY,
                    new Object[]{ELEMENT_TYPE, ID_FIELD_NAME}
            );
        }

        List<Utilisateur> foundUtilisateurs = utilisateurRepository.findAllById(codes);

        Set<String> foundIds = foundUtilisateurs.stream()
                .map(Utilisateur::getCode)
                .collect(Collectors.toSet());

        if (foundIds.size() != codes.size()) {
            throw new ElementNotFoundException(
                    CoreConstants.BusinessExceptionMessage.MANY_NOT_FOUND,
                    new Object[]{ELEMENT_TYPE},
                    codes.stream()
                            .filter(code -> !foundIds.contains(code))
                            .toList()
            );
        }

        utilisateurMapper.updateRequestToUtilisateurList(requestList, foundUtilisateurs);


        checkForeignKeys(foundUtilisateurs);

        return utilisateurMapper.toUtilisateurResponseList(
                utilisateurRepository.saveAll(foundUtilisateurs)
        );
    }

    @Override
    public void deleteById(String code) throws ElementNotFoundException {
        if (!utilisateurRepository.existsById(code)) {
            throw new ElementNotFoundException(
                    CoreConstants.BusinessExceptionMessage.NOT_FOUND,
                    new Object[]{ELEMENT_TYPE, ID_FIELD_NAME, code},
                    null
            );
        }
        filiereClient.handleChefFiliereDeletion(Set.of(code));
        elementClient.handleProfesseurDeletion(Set.of(code));
        utilisateurRepository.deleteById(code);
    }

    @Override
    public void deleteAllById(Set<String> codeList) throws ElementNotFoundException {
        this.exists(codeList);
        filiereClient.handleChefFiliereDeletion(codeList);
        elementClient.handleProfesseurDeletion(codeList);
        utilisateurRepository.deleteAllById(codeList);
    }


    @Override
    public boolean exists(Set<String> codeList) {
        Set<String> codes = utilisateurRepository.findAllById(codeList)
                .stream()
                .map(Utilisateur::getCode)
                .collect(Collectors.toSet());

        if (codes.size() != codeList.size()) {
            throw new ElementNotFoundException(
                    CoreConstants.BusinessExceptionMessage.MANY_NOT_FOUND,
                    new Object[]{ELEMENT_TYPE},
                    codeList.stream()
                            .filter(code -> !codes.contains(code))
                            .collect(Collectors.toList())
            );
        }

        return true;
    }


    private void checkForeignKeys(Utilisateur request) {
        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            Set<String> roleCodes = request.getRoles().stream()
                    .map(Role::getRoleId)
                    .collect(Collectors.toSet());
            roleService.exists(roleCodes);
        }
        if (request.getCodeDepartement() != null) {
            departementClient.existsAll(
                    Set.of(request.getCodeDepartement())
            );
        }
    }

    private void checkForeignKeys(List<Utilisateur> requestList) {

        Set<String> roleCodes = requestList.stream()
                .map(
                        utilisateur -> {
                            if (utilisateur.getRoles() != null && !utilisateur.getRoles().isEmpty()) {
                                return utilisateur.getRoles().stream()
                                        .map(Role::getRoleId)
                                        .collect(Collectors.toSet());
                            }
                            return new HashSet<String>();
                        }
                )
                .flatMap(Set::stream)
                .collect(Collectors.toSet());


        if (!roleCodes.isEmpty())
            roleService.exists(roleCodes);

        Set<String> departementCodes = requestList.stream()
                .map(Utilisateur::getCodeDepartement)
                .collect(Collectors.toSet());
        if (!departementCodes.isEmpty()) {
            departementClient.existsAll(departementCodes);
        }

    }

    private void handleNested(UtilisateurResponse utilisateur, IncludeParams includes) {

        if (includes.isIncludePermissions()) {
            Set<PermissionResponse> permissions = new HashSet<>();
            utilisateur.getRoles().forEach(roleResponse -> {
                permissions.addAll(roleResponse.getPermissions());
            });
            utilisateur.setPermissions(permissions.stream().toList());
        } else {
            utilisateur.setPermissions(null);
        }

        if (includes.isIncludeRoles()) {
            utilisateur.getRoles().forEach(roleResponse -> {
                roleResponse.setPermissions(null);
            });
        } else {
            utilisateur.setRoles(null);
        }

        if (includes.isIncludeDepartement() && utilisateur.getCodeDepartement() != null) {
            utilisateur.setDepartement(
                    departementClient.get(utilisateur.getCodeDepartement()).getBody()
            );
        }

        if (includes.isIncludeElements()) {
            utilisateur.setElements(
                    elementClient.getAllByCodeProfesseur(utilisateur.getCode()).getBody()
            );
        }

    }

    private void handleNested(List<UtilisateurResponse> utilisateurs, IncludeParams includes) {
        if (includes.isIncludePermissions()) {
            utilisateurs.forEach(utilisateurResponse -> {
                Set<PermissionResponse> permissions = new HashSet<>();
                utilisateurResponse.getRoles().forEach(roleResponse -> {
                    permissions.addAll(roleResponse.getPermissions());
                });
                utilisateurResponse.setPermissions(permissions.stream().toList());
            });
        } else {
            utilisateurs.forEach(utilisateurResponse -> {
                utilisateurResponse.setPermissions(null);
            });
        }

        if (includes.isIncludeRoles()) {
            utilisateurs.forEach(utilisateurResponse -> {
                utilisateurResponse.getRoles().forEach(roleResponse -> {
                    roleResponse.setPermissions(null);
                });
            });
        } else {
            utilisateurs.forEach(utilisateurResponse -> {
                utilisateurResponse.setRoles(null);
            });
        }


        if (includes.isIncludeDepartement()) {
            Set<String> codesDepartement = new HashSet<>();
            utilisateurs.forEach(
                    utilisateurResponse -> {
                        if (utilisateurResponse.getCodeDepartement() != null) {
                            codesDepartement.add(utilisateurResponse.getCodeDepartement());
                        }
                    }
            );

            if (!codesDepartement.isEmpty()) {
                List<DepartementResponse> departementResponses = departementClient.getAllById(codesDepartement).getBody();
                if (departementResponses != null) {
                    utilisateurs.forEach(
                            utilisateurResponse -> {
                                utilisateurResponse.setDepartement(
                                        departementResponses.stream()
                                                .filter(
                                                        departementResponse -> departementResponse.getCodeDepartement()
                                                                .equals(
                                                                        utilisateurResponse.getCodeDepartement()
                                                                )
                                                )
                                                .findFirst()
                                                .orElse(null)
                                );
                            }
                    );
                }
            }
        }

        if (includes.isIncludeElements()) {
            Set<String> codes = new HashSet<>();
            utilisateurs.forEach(
                    utilisateurResponse -> {
                        if (utilisateurResponse.getCode() != null) {
                            codes.add(utilisateurResponse.getCode());
                        }
                    }
            );
            if (!codes.isEmpty()) {
                List<GroupedElementsResponse> groupedElementsResponses = elementClient.getAllByCodesProfesseur(codes).getBody();
                if (groupedElementsResponses != null) {
                    utilisateurs.forEach(
                            utilisateurResponse -> {
                                utilisateurResponse.setElements(
                                        groupedElementsResponses.stream()
                                                .filter(
                                                        groupedElementsResponse -> groupedElementsResponse.codeProfesseur().equals(utilisateurResponse.getCode())
                                                )
                                                .map(GroupedElementsResponse::elements)
                                                .flatMap(List::stream)
                                                .toList()
                                );
                            }
                    );
                }
            }
        }

    }

    @Override
    public void handleKeyDepartementDeletion(Set<String> codeDepartement) {
        List<Utilisateur> utilisateurs = utilisateurRepository.findAllByCodeDepartementIn(codeDepartement);
        if (utilisateurs != null) {
            utilisateurs.forEach(
                    utilisateur -> {
                        if (codeDepartement.contains(utilisateur.getCodeDepartement())) {
                            utilisateur.setCodeDepartement(null);
                        }
                    }
            );
            utilisateurRepository.saveAll(utilisateurs);
        }
    }
}
