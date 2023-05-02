package ma.enset.utilisateur.service;

import jakarta.transaction.Transactional;
import jdk.jshell.execution.Util;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.utilisateur.client.ElementClient;
import ma.enset.utilisateur.constant.CoreConstants;
import ma.enset.utilisateur.dto.ElementByCodeProfesseurResponse;
import ma.enset.utilisateur.dto.ElementResponse;
import ma.enset.utilisateur.exception.*;
import ma.enset.utilisateur.model.Role;
import ma.enset.utilisateur.model.Utilisateur;
import ma.enset.utilisateur.repository.UtilisateurRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class UtilisateurServiceImpl implements UtilisateurService {
    private final UtilisateurRepository utilisateurRepository;
    private final RoleService roleService;

    private final ElementClient elementClient;


    @Override
    public Utilisateur save(Utilisateur utilisateur, String roleId) throws ElementAlreadyExistsException, InternalErrorException {
        if (utilisateurRepository.existsByCode(utilisateur.getCode()))
            throw utilisateurAlreadyExistsException(utilisateur.getCode());

        Role role = roleService.findByRoleId(roleId);
        utilisateur.setRoles(List.of(role));

        Utilisateur savedUtilisateur = null;

        try {
            savedUtilisateur = utilisateurRepository.save(utilisateur);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            throw new InternalErrorException();
        }

        return savedUtilisateur;
    }

    @Override
    public Utilisateur save(Utilisateur utilisateur) throws ElementAlreadyExistsException, InternalErrorException {
        if (utilisateurRepository.existsByCode(utilisateur.getCode()))
            throw utilisateurAlreadyExistsException(utilisateur.getCode());

        utilisateur.getRoles().forEach(role ->
                roleService.findByRoleId(role.getRoleId())
        );

        Utilisateur savedUtilisateur = null;

        try {
            savedUtilisateur = utilisateurRepository.save(utilisateur);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            throw new InternalErrorException();
        }

        return savedUtilisateur;
    }

    @Transactional
    @Override
    public List<Utilisateur> saveAll(List<Utilisateur> utilisateurs, String roleId) throws ElementAlreadyExistsException, InternalErrorException {
        List<Utilisateur> savedUtilisateurs = new ArrayList<>();
        utilisateurs.forEach(utilisateur -> savedUtilisateurs.add(save(utilisateur, roleId)));
        return savedUtilisateurs;
    }

    @Transactional
    @Override
    public List<Utilisateur> saveAll(List<Utilisateur> utilisateurs) throws ElementAlreadyExistsException, InternalErrorException {
        List<Utilisateur> savedUtilisateurs = new ArrayList<>();
        utilisateurs.forEach(utilisateur -> savedUtilisateurs.add(save(utilisateur)));
        return savedUtilisateurs;
    }

    @Override
    public Page<Utilisateur> findAll(Pageable pageable) {
        return utilisateurRepository.findAll(pageable);
    }

    @Override
    public Page<Utilisateur> findAll(Pageable pageable, String roleId) throws ElementNotFoundException {
        Role role = roleService.findByRoleId(roleId);
        Page<Utilisateur> utilisateurs = utilisateurRepository.findAllByRolesContains(role, pageable);
        List<String> utilisateursCodes = utilisateurs.getContent().stream().map(Utilisateur::getCode).toList();
        log.info("Utilisateurs codes: {}", utilisateursCodes);
        List<ElementByCodeProfesseurResponse> elements = this.getElements(utilisateursCodes);
        for(int i = 0; i < utilisateursCodes.size(); i++) {
            utilisateurs.getContent().get(i).setElements(elements.get(i).elements());
        }
        return utilisateurs;
    }


    @Override
    public Utilisateur update(Utilisateur utilisateur, String roleId) throws ElementNotFoundException, RoleConflictException, InternalErrorException {

        Utilisateur toBeUpdated = this.getUtilisateurAndCheckRole(utilisateur.getCode(), roleId);

        Utilisateur updatedUtilisateur = null;

        try {
            updatedUtilisateur = utilisateurRepository.save(toBeUpdated);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            throw new InternalErrorException();
        }

        return updatedUtilisateur;
    }

    @Override
    public Utilisateur update(Utilisateur utilisateur) throws ElementNotFoundException, RoleConflictException, InternalErrorException {

        Utilisateur toBeUpdated = this.getUtilisateur(utilisateur.getCode());

        List<String> rolesIds = utilisateur.getRoles().stream().map(Role::getRoleId).toList();

        List<Role> roles = roleService.findAllByRoleId(rolesIds);

        toBeUpdated.setRoles(roles);

        Utilisateur updatedUtilisateur = null;

        try {
            updatedUtilisateur = utilisateurRepository.save(toBeUpdated);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            throw new InternalErrorException();
        }

        return updatedUtilisateur;
    }

    @Transactional
    @Override
    public List<Utilisateur> updateAll(List<Utilisateur> utilisateurs, String roleId) throws ElementNotFoundException, RoleConflictException, InternalErrorException {
        List<Utilisateur> updatedUtilisateurs = new ArrayList<>();
        utilisateurs.forEach(utilisateur -> updatedUtilisateurs.add(update(utilisateur, roleId)));
        return updatedUtilisateurs;
    }

    @Transactional
    @Override
    public List<Utilisateur> updateAll(List<Utilisateur> utilisateurs) throws ElementNotFoundException, RoleConflictException, InternalErrorException {
        List<Utilisateur> updatedUtilisateurs = new ArrayList<>();
        utilisateurs.forEach(utilisateur -> updatedUtilisateurs.add(update(utilisateur)));
        return updatedUtilisateurs;
    }


    @Override
    public void deleteByCodeUtilisateur(String codeUtilisateur, String roleId) throws ElementNotFoundException {
        this.getUtilisateurAndCheckRole(codeUtilisateur, roleId);
        utilisateurRepository.deleteByCode(codeUtilisateur);
    }

    @Override
    public void deleteByCodeUtilisateur(String codeUtilisateur) throws ElementNotFoundException {
        this.getUtilisateur(codeUtilisateur);
        utilisateurRepository.deleteByCode(codeUtilisateur);
    }

    @Transactional
    @Override
    public void deleteAllByCodeUtilisateur(List<String> codeUtilisateurs, String roleId) throws ElementNotFoundException {
        for (String codeUtilisateur : codeUtilisateurs) {
            this.deleteByCodeUtilisateur(codeUtilisateur, roleId);
        }
    }

    @Transactional
    @Override
    public void deleteAllByCodeUtilisateur(List<String> codeUtilisateurs) throws ElementNotFoundException {
        for (String codeUtilisateur : codeUtilisateurs) {
            this.deleteByCodeUtilisateur(codeUtilisateur);
        }
    }

    @Override
    public Utilisateur findByCodeUtilisateur(String codeUtilisateur, String roleId) throws ElementNotFoundException {
//
//        Role role = Role.builder()
//                .roleId(roleId)
//                .build();
//
        return this.getUtilisateurAndCheckRole(codeUtilisateur, roleId);
    }

    @Override
    public Utilisateur findByCodeUtilisateur(String codeUtilisateur) throws ElementNotFoundException {
        return utilisateurRepository.findByCode(codeUtilisateur)
                .orElseThrow(() ->
                        ElementNotFoundException.builder()
                                .key(CoreConstants.BusinessExceptionMessage.UTILISATEUR_NOT_FOUND)
                                .args(new Object[]{"Utilisateur", codeUtilisateur})
                                .build()
                );
    }


    @Override
    public List<Utilisateur> findAllByCodeUtilisateur(List<String> codeUtilisateurs, String roleId) throws ElementNotFoundException {
        List<Utilisateur> elements = new ArrayList<>();
        for (String codeUtilisateur : codeUtilisateurs) {
            elements.add(findByCodeUtilisateur(codeUtilisateur, roleId));
        }
        return elements;
    }


    @Override
    public List<Utilisateur> findAllByCodeUtilisateur(List<String> codeUtilisateurs) throws ElementNotFoundException {
        List<Utilisateur> elements = new ArrayList<>();
        for (String codeUtilisateur : codeUtilisateurs) {
            elements.add(findByCodeUtilisateur(codeUtilisateur));
        }
        return elements;
    }

    private Utilisateur getUtilisateur(String codeUtilisateur) throws ElementNotFoundException {

        Utilisateur utilisateur = utilisateurRepository.findByCode(codeUtilisateur).orElse(null);

        if (utilisateur == null)
            throw utilisateurNotFoundException(codeUtilisateur);


        return utilisateur;
    }

    private Utilisateur getUtilisateurAndCheckRole(String codeUtlisateur, String roleId) throws ElementNotFoundException, RoleConflictException {

        Utilisateur utilisateur = getUtilisateur(codeUtlisateur);

        if (utilisateur.getRoles().stream().map(Role::getRoleId).noneMatch(roleName -> roleName.equals(roleId)))
            throw RoleConflictException.builder()
                    .key(CoreConstants.BusinessExceptionMessage.ROLE_CONFLICT)
                    .args(new Object[]{codeUtlisateur, roleId})
                    .build();

        if(utilisateur.getRoles().stream().map(Role::getRoleId).anyMatch(roleName -> roleName.equals(CoreConstants.RoleID.ROLE_PROFESSEUR))){
            ElementByCodeProfesseurResponse elementResponse = getElement(codeUtlisateur);
            utilisateur.setElements(elementResponse.elements());
        }

        return utilisateur;
    }

    private ElementAlreadyExistsException utilisateurAlreadyExistsException(String codeUtilisateur) {
        return ElementAlreadyExistsException.builder()
                .key(CoreConstants.BusinessExceptionMessage.UTILISATEUR_ALREADY_EXISTS)
                .args(new Object[]{"Utilisateur", codeUtilisateur})
                .build();
    }

    private ElementNotFoundException utilisateurNotFoundException(String codeUtilisateur) {
        return ElementNotFoundException.builder()
                .key(CoreConstants.BusinessExceptionMessage.UTILISATEUR_NOT_FOUND)
                .args(new Object[]{"Utilisateur", codeUtilisateur})
                .build();
    }

    private ElementByCodeProfesseurResponse getElement(String codeElement) throws ExchangerException{
        ElementByCodeProfesseurResponse elementResponse;
        try{
            elementResponse = elementClient.getElementsByCodeProfesseur(codeElement).getBody();
        }catch (HttpClientErrorException | HttpServerErrorException e) {
            throw ExchangerException.builder()
                    .exceptionBody(e.getResponseBodyAsString())
                    .build();
        }
        return elementResponse;
    }

    private List<ElementByCodeProfesseurResponse> getElements(List<String> codesElement) throws ExchangerException{
        List<ElementByCodeProfesseurResponse> elementResponses;
        try {
            elementResponses = elementClient.getElementsByCodeProfesseurs(codesElement).getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw ExchangerException.builder()
                    .exceptionBody(e.getResponseBodyAsString())
                    .build();
        }
        return elementResponses;
    }

}
