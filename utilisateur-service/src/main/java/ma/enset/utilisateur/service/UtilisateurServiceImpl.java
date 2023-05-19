package ma.enset.utilisateur.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.utilisateur.constant.CoreConstants;
import ma.enset.utilisateur.dto.IncludeParams;
import ma.enset.utilisateur.dto.PagingResponse;
import ma.enset.utilisateur.dto.permission.PermissionResponse;
import ma.enset.utilisateur.dto.role.RoleResponse;
import ma.enset.utilisateur.dto.utilisateur.UtilisateurCreateRequest;
import ma.enset.utilisateur.dto.utilisateur.UtilisateurResponse;
import ma.enset.utilisateur.dto.utilisateur.UtilisateurUpdateRequest;
import ma.enset.utilisateur.exception.*;
import ma.enset.utilisateur.mapper.UtilisateurMapper;
import ma.enset.utilisateur.model.Permission;
import ma.enset.utilisateur.model.Role;
import ma.enset.utilisateur.model.Utilisateur;
import ma.enset.utilisateur.repository.UtilisateurRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
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

        utilisateurRepository.deleteById(code);
    }

    @Override
    public void deleteAllById(Set<String> codeList) throws ElementNotFoundException {
        this.exists(codeList);
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
        if (request.getCodeFiliere() != null) {
            // TODO (ahmed) : check filiere
        }
        if (request.getCodeDepartement() != null) {
            // TODO (ahmed) : check departement
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

        Set<String> filiereCodes = requestList.stream()
                .map(Utilisateur::getCodeFiliere)
                .collect(Collectors.toSet());
        // TODO (ahmed) : check filiere

        Set<String> departementCodes = requestList.stream()
                .map(Utilisateur::getCodeDepartement)
                .collect(Collectors.toSet());
        // TODO (ahmed) : check departement

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

        if (includes.isIncludeFiliere()) {
            // TODO (ahmed) : include filiere
        }

        if (includes.isIncludeDepartement()) {
            // TODO (ahmed) : include departement
        }

        if (includes.isIncludeElements()) {
            // TODO (ahmed) : include elements
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

        if (includes.isIncludeFiliere()) {
            // TODO (ahmed) : include filiere
        }

        if (includes.isIncludeDepartement()) {
            // TODO (ahmed) : include departement
        }

        if (includes.isIncludeElements()) {
            // TODO (ahmed) : include elements
        }

    }

    //
//
//    @Override
//    public Utilisateur save(Utilisateur utilisateur, String roleId) throws ElementAlreadyExistsException, InternalErrorException {
//        if (utilisateurRepository.existsByCode(utilisateur.getCode()))
//            throw utilisateurAlreadyExistsException(utilisateur.getCode(), roleId);
//
//        Role role = roleService.findByRoleId(roleId);
//        utilisateur.setRoles(List.of(role));
//
//        Utilisateur savedUtilisateur = null;
//
//        try {
//            savedUtilisateur = utilisateurRepository.save(utilisateur);
//        } catch (Exception e) {
//            log.error(e.getMessage(), e.getCause());
//            throw new InternalErrorException();
//        }
//
//        return savedUtilisateur;
//    }
//
//    @Override
//    public Utilisateur save(Utilisateur utilisateur) throws ElementAlreadyExistsException, InternalErrorException {
//        if (utilisateurRepository.existsByCode(utilisateur.getCode()))
//            throw utilisateurAlreadyExistsException(utilisateur.getCode());
//
//        utilisateur.getRoles().forEach(role ->
//                roleService.findByRoleId(role.getRoleId())
//        );
//
//        Utilisateur savedUtilisateur = null;
//
//        try {
//            savedUtilisateur = utilisateurRepository.save(utilisateur);
//        } catch (Exception e) {
//            log.error(e.getMessage(), e.getCause());
//            throw new InternalErrorException();
//        }
//
//        return savedUtilisateur;
//    }
//
//    @Transactional
//    @Override
//    public List<Utilisateur> saveAll(List<Utilisateur> utilisateurs, String roleId) throws ElementAlreadyExistsException, InternalErrorException {
//
//        utilisateurs.forEach(utilisateur -> {
//            if(utilisateurRepository.existsByCode(utilisateur.getCode()))
//                throw utilisateurAlreadyExistsException(utilisateur.getCode(), roleId);
//            Role role = roleService.findByRoleId(roleId);
//            utilisateur.setRoles(List.of(role));
//        });
//
//        List<Utilisateur> savedUtilisateurs;
//        try{
//            savedUtilisateurs = utilisateurRepository.saveAll(utilisateurs);
//        }catch (Exception e) {
//            log.error(e.getMessage(), e.getCause());
//            throw new InternalErrorException();
//        }
//        return savedUtilisateurs;
//    }
//
//    @Transactional
//    @Override
//    public List<Utilisateur> saveAll(List<Utilisateur> utilisateurs) throws ElementAlreadyExistsException, InternalErrorException {
//        utilisateurs.forEach(utilisateur -> {
//            if(utilisateurRepository.existsByCode(utilisateur.getCode()))
//                throw utilisateurAlreadyExistsException(utilisateur.getCode());
//        });
//        List<Utilisateur> savedUtilisateurs;
//        try{
//            savedUtilisateurs = utilisateurRepository.saveAll(utilisateurs);
//        }catch (Exception e) {
//            log.error(e.getMessage(), e.getCause());
//            throw new InternalErrorException();
//        }
//        return savedUtilisateurs;
//    }
//
//    @Override
//    public Page<Utilisateur> findAll(Pageable pageable) {
//        return utilisateurRepository.findAll(pageable);
//    }
//
//    @Override
//    public Page<Utilisateur> findAll(Pageable pageable, String roleId) throws ElementNotFoundException {
//        Role role = roleService.findByRoleId(roleId);
//        Page<Utilisateur> utilisateurs = utilisateurRepository.findAllByRolesContains(role, pageable);
//        List<String> utilisateursCodes = utilisateurs.getContent().stream().map(Utilisateur::getCode).toList();
//
//        List<ElementByCodeProfesseurResponse> elements = this.getElements(utilisateursCodes);
//        for(int i = 0; i < utilisateursCodes.size(); i++) {
//            utilisateurs.getContent().get(i).setElements(elements.get(i).elements());
//        }
//        return utilisateurs;
//    }
//
//
//    @Override
//    public Utilisateur update(Utilisateur utilisateur, String roleId) throws ElementNotFoundException, InternalErrorException {
//
//        Utilisateur toBeUpdated = this.getUtilisateurAndCheckRole(utilisateur.getCode(), roleId);
//
//        Utilisateur updatedUtilisateur = null;
//
//        try {
//            updatedUtilisateur = utilisateurRepository.save(toBeUpdated);
//        } catch (Exception e) {
//            log.error(e.getMessage(), e.getCause());
//            throw new InternalErrorException();
//        }
//
//        return updatedUtilisateur;
//    }
//
//    @Override
//    public Utilisateur update(Utilisateur utilisateur) throws ElementNotFoundException, InternalErrorException {
//
//        Utilisateur toBeUpdated = checkUtilisateurAndSetRoles(utilisateur);
//
//        Utilisateur updatedUtilisateur = null;
//
//        try {
//            updatedUtilisateur = utilisateurRepository.save(toBeUpdated);
//        } catch (Exception e) {
//            log.error(e.getMessage(), e.getCause());
//            throw new InternalErrorException();
//        }
//
//        return updatedUtilisateur;
//    }
//
//    @Transactional
//    @Override
//    public List<Utilisateur> updateAll(List<Utilisateur> utilisateurs, String roleId) throws ElementNotFoundException, InternalErrorException {
//
//        utilisateurs.forEach(utilisateur -> this.getUtilisateurAndCheckRole(utilisateur.getCode(), roleId));
//
//        List<Utilisateur> updatedUtilisateurs;
//        try{
//            updatedUtilisateurs = utilisateurRepository.saveAll(utilisateurs);
//        }catch (Exception e) {
//            log.error(e.getMessage(), e.getCause());
//            throw new InternalErrorException();
//        }
//        return updatedUtilisateurs;
//    }
//
//    @Transactional
//    @Override
//    public List<Utilisateur> updateAll(List<Utilisateur> utilisateurs) throws ElementNotFoundException, InternalErrorException {
//        List<Utilisateur> toBeUpdated = utilisateurs.stream().map(this::checkUtilisateurAndSetRoles).toList();
//
//        List<Utilisateur> updatedUtilisateurs;
//        try{
//            updatedUtilisateurs = utilisateurRepository.saveAll(toBeUpdated);
//        }catch (Exception e) {
//            log.error(e.getMessage(), e.getCause());
//            throw new InternalErrorException();
//        }
//        return updatedUtilisateurs;
//    }
//
//
//    @Override
//    public void deleteByCodeUtilisateur(String codeUtilisateur, String roleId) throws ElementNotFoundException {
//        this.getUtilisateurAndCheckRole(codeUtilisateur, roleId);
//        utilisateurRepository.deleteByCode(codeUtilisateur);
//    }
//
//    @Override
//    public void deleteByCodeUtilisateur(String codeUtilisateur) throws ElementNotFoundException {
//        utilisateurRepository.findById(codeUtilisateur).orElseThrow(() -> utilisateurNotFoundException(codeUtilisateur));
//        utilisateurRepository.deleteByCode(codeUtilisateur);
//    }
//
//    @Transactional
//    @Override
//    public void deleteAllByCodeUtilisateur(List<String> codeUtilisateurs, String roleId) throws ElementNotFoundException {
//        for (String codeUtilisateur : codeUtilisateurs) {
//            this.deleteByCodeUtilisateur(codeUtilisateur, roleId);
//        }
//    }
//
//    @Transactional
//    @Override
//    public void deleteAllByCodeUtilisateur(List<String> codeUtilisateurs) throws ElementNotFoundException {
//        for (String codeUtilisateur : codeUtilisateurs) {
//            this.deleteByCodeUtilisateur(codeUtilisateur);
//        }
//    }
//
//    @Override
//    public Utilisateur findByCodeUtilisateur(String codeUtilisateur, String roleId) throws ElementNotFoundException {
////
////        Role role = Role.builder()
////                .roleId(roleId)
////                .build();
////
//        return this.getUtilisateurAndCheckRole(codeUtilisateur, roleId);
//    }
//
//    @Override
//    public Utilisateur findByCodeUtilisateur(String codeUtilisateur) throws ElementNotFoundException {
//        return utilisateurRepository.findByCode(codeUtilisateur)
//                .orElseThrow(() ->
//                        ElementNotFoundException.builder()
//                                .key(CoreConstants.BusinessExceptionMessage.UTILISATEUR_NOT_FOUND)
//                                .args(new Object[]{"Utilisateur", codeUtilisateur})
//                                .build()
//                );
//    }
//
//
//    @Override
//    public List<Utilisateur> findAllByCodeUtilisateur(List<String> codeUtilisateurs, String roleId) throws ElementNotFoundException {
//        List<Utilisateur> elements = new ArrayList<>();
//        for (String codeUtilisateur : codeUtilisateurs) {
//            elements.add(findByCodeUtilisateur(codeUtilisateur, roleId));
//        }
//        return elements;
//    }
//
//
//    @Override
//    public List<Utilisateur> findAllByCodeUtilisateur(List<String> codeUtilisateurs) throws ElementNotFoundException {
//        List<Utilisateur> elements = new ArrayList<>();
//        for (String codeUtilisateur : codeUtilisateurs) {
//            elements.add(findByCodeUtilisateur(codeUtilisateur));
//        }
//        return elements;
//    }
//
//    private Utilisateur checkUtilisateurAndSetRoles(Utilisateur utilisateur) throws ElementNotFoundException {
//        utilisateurRepository.findByCode(utilisateur.getCode())
//                .orElseThrow(() -> utilisateurNotFoundException(utilisateur.getCode()));
//        List<String> rolesIds = utilisateur.getRoles().stream().map(Role::getRoleId).toList();
//        List<Role> roles = roleService.findAllByRoleId(rolesIds);
//        utilisateur.setRoles(roles);
//        return utilisateur;
//    }
//
//
//    private Utilisateur getUtilisateurAndCheckRole(String codeUtilisateur, String roleId) throws ElementNotFoundException {
//
//        Utilisateur utilisateur = utilisateurRepository.findByCode(codeUtilisateur).orElseThrow(() -> utilisateurNotFoundException(codeUtilisateur,roleId));
//
//        if (utilisateur.getRoles().stream().map(Role::getRoleId).noneMatch(roleName -> roleName.equals(roleId)))
//            throw utilisateurNotFoundException(codeUtilisateur,roleId);
//
//        if(utilisateur.getRoles().stream().map(Role::getRoleId).anyMatch(roleName -> roleName.equals(CoreConstants.RoleID.ROLE_PROFESSEUR))){
//            ElementByCodeProfesseurResponse elementResponse = getElement(codeUtilisateur);
//            utilisateur.setElements(elementResponse.elements());
//        }
//
//        return utilisateur;
//    }
//
//    private ElementAlreadyExistsException utilisateurAlreadyExistsException(String codeUtilisateur) {
//        return ElementAlreadyExistsException.builder()
//                .key(CoreConstants.BusinessExceptionMessage.UTILISATEUR_ALREADY_EXISTS)
//                .args(new Object[]{"Utilisateur", codeUtilisateur})
//                .build();
//    }
//
//    private ElementNotFoundException utilisateurNotFoundException(String codeUtilisateur) {
//        return ElementNotFoundException.builder()
//                .key(CoreConstants.BusinessExceptionMessage.UTILISATEUR_NOT_FOUND)
//                .args(new Object[]{"Utilisateur", codeUtilisateur})
//                .build();
//    }
//
//    private ElementAlreadyExistsException utilisateurAlreadyExistsException(String codeUtilisateur,String roleID) {
//        return ElementAlreadyExistsException.builder()
//                .key(CoreConstants.BusinessExceptionMessage.UTILISATEUR_ALREADY_EXISTS)
//                .args(new Object[]{roleID, codeUtilisateur})
//                .build();
//    }
//
//    private ElementNotFoundException utilisateurNotFoundException(String codeUtilisateur,String roleID) {
//        return ElementNotFoundException.builder()
//                .key(CoreConstants.BusinessExceptionMessage.UTILISATEUR_NOT_FOUND)
//                .args(new Object[]{roleID, codeUtilisateur})
//                .build();
//    }
//
//    private ElementByCodeProfesseurResponse getElement(String codeElement) throws ExchangerException{
//        ElementByCodeProfesseurResponse elementResponse;
//        try{
//            elementResponse = elementClient.getElementsByCodeProfesseur(codeElement).getBody();
//        }catch (HttpClientErrorException | HttpServerErrorException e) {
//            throw ExchangerException.builder()
//                    .exceptionBody(e.getResponseBodyAsString())
//                    .build();
//        }
//        return elementResponse;
//    }
//
//    private List<ElementByCodeProfesseurResponse> getElements(List<String> codesElement) throws ExchangerException{
//        List<ElementByCodeProfesseurResponse> elementResponses;
//        try {
//            elementResponses = elementClient.getElementsByCodeProfesseurs(codesElement).getBody();
//        } catch (HttpClientErrorException | HttpServerErrorException e) {
//            throw ExchangerException.builder()
//                    .exceptionBody(e.getResponseBodyAsString())
//                    .build();
//        }
//        return elementResponses;
//    }

}
