package ma.enset.utilisateur.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.utilisateur.client.ElementClient;
import ma.enset.utilisateur.constant.CoreConstants;
import ma.enset.utilisateur.dto.ElementByCodeProfesseurResponse;
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
            throw utilisateurAlreadyExistsException(utilisateur.getCode(), roleId);

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

        utilisateurs.forEach(utilisateur -> {
            if(utilisateurRepository.existsByCode(utilisateur.getCode()))
                throw utilisateurAlreadyExistsException(utilisateur.getCode(), roleId);
            Role role = roleService.findByRoleId(roleId);
            utilisateur.setRoles(List.of(role));
        });

        List<Utilisateur> savedUtilisateurs;
        try{
            savedUtilisateurs = utilisateurRepository.saveAll(utilisateurs);
        }catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            throw new InternalErrorException();
        }
        return savedUtilisateurs;
    }

    @Transactional
    @Override
    public List<Utilisateur> saveAll(List<Utilisateur> utilisateurs) throws ElementAlreadyExistsException, InternalErrorException {
        utilisateurs.forEach(utilisateur -> {
            if(utilisateurRepository.existsByCode(utilisateur.getCode()))
                throw utilisateurAlreadyExistsException(utilisateur.getCode());
        });
        List<Utilisateur> savedUtilisateurs;
        try{
            savedUtilisateurs = utilisateurRepository.saveAll(utilisateurs);
        }catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            throw new InternalErrorException();
        }
        return savedUtilisateurs;
    }

    @Override
    public Page<Utilisateur> findAll(String search,Pageable pageable) {
        return utilisateurRepository.findAllWithSearch(search,pageable);
    }

    @Override
    public Page<Utilisateur> findAll(String search,Pageable pageable, String roleId) throws ElementNotFoundException {
        Role role = roleService.findByRoleId(roleId);
        Page<Utilisateur> utilisateurs = utilisateurRepository.findAllWithSearchAndRole(search,role, pageable);

        if(utilisateurs.hasContent() && roleId.equals(CoreConstants.RoleID.ROLE_PROFESSEUR)){
            System.out.println("professeur");
            List<String> utilisateursCodes = utilisateurs.getContent().stream().map(Utilisateur::getCode).toList();

            List<ElementByCodeProfesseurResponse> elements = this.getElements(utilisateursCodes);
            for(int i = 0; i < utilisateursCodes.size(); i++) {
                utilisateurs.getContent().get(i).setElements(elements.get(i).elements());
            }
        }

        return utilisateurs;
    }


    @Override
    public Utilisateur update(Utilisateur utilisateur, String roleId) throws ElementNotFoundException, InternalErrorException {

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
    public Utilisateur update(Utilisateur utilisateur) throws ElementNotFoundException, InternalErrorException {

        Utilisateur toBeUpdated = checkUtilisateurAndSetRoles(utilisateur);

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
    public List<Utilisateur> updateAll(List<Utilisateur> utilisateurs, String roleId) throws ElementNotFoundException, InternalErrorException {

        utilisateurs.forEach(utilisateur -> this.getUtilisateurAndCheckRole(utilisateur.getCode(), roleId));

        List<Utilisateur> updatedUtilisateurs;
        try{
            updatedUtilisateurs = utilisateurRepository.saveAll(utilisateurs);
        }catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            throw new InternalErrorException();
        }
        return updatedUtilisateurs;
    }

    @Transactional
    @Override
    public List<Utilisateur> updateAll(List<Utilisateur> utilisateurs) throws ElementNotFoundException, InternalErrorException {
        List<Utilisateur> toBeUpdated = utilisateurs.stream().map(this::checkUtilisateurAndSetRoles).toList();

        List<Utilisateur> updatedUtilisateurs;
        try{
            updatedUtilisateurs = utilisateurRepository.saveAll(toBeUpdated);
        }catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            throw new InternalErrorException();
        }
        return updatedUtilisateurs;
    }


    @Override
    public void deleteByCodeUtilisateur(String codeUtilisateur, String roleId) throws ElementNotFoundException {
        this.getUtilisateurAndCheckRole(codeUtilisateur, roleId);
        utilisateurRepository.deleteByCode(codeUtilisateur);
    }

    @Override
    public void deleteByCodeUtilisateur(String codeUtilisateur) throws ElementNotFoundException {
        utilisateurRepository.findById(codeUtilisateur).orElseThrow(() -> utilisateurNotFoundException(codeUtilisateur));
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

    private Utilisateur checkUtilisateurAndSetRoles(Utilisateur utilisateur) throws ElementNotFoundException {
        utilisateurRepository.findByCode(utilisateur.getCode())
                .orElseThrow(() -> utilisateurNotFoundException(utilisateur.getCode()));
        List<String> rolesIds = utilisateur.getRoles().stream().map(Role::getRoleId).toList();
        List<Role> roles = roleService.findAllByRoleId(rolesIds);
        utilisateur.setRoles(roles);
        return utilisateur;
    }


    private Utilisateur getUtilisateurAndCheckRole(String codeUtilisateur, String roleId) throws ElementNotFoundException {

        Utilisateur utilisateur = utilisateurRepository.findByCode(codeUtilisateur).orElseThrow(() -> utilisateurNotFoundException(codeUtilisateur,roleId));
        System.out.println("here");
        log.warn(utilisateur.getRoles().stream().map(Role::getRoleId).toList().toString());
        if (utilisateur.getRoles().stream().map(Role::getRoleId).noneMatch(roleName -> roleName.equals(roleId)))
            throw utilisateurNotFoundException(codeUtilisateur,roleId);

        if(utilisateur.getRoles().stream().map(Role::getRoleId).anyMatch(roleName -> roleName.equals(CoreConstants.RoleID.ROLE_PROFESSEUR))){
            ElementByCodeProfesseurResponse elementResponse = getElement(codeUtilisateur);
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

    private ElementAlreadyExistsException utilisateurAlreadyExistsException(String codeUtilisateur,String roleID) {
        return ElementAlreadyExistsException.builder()
                .key(CoreConstants.BusinessExceptionMessage.UTILISATEUR_ALREADY_EXISTS)
                .args(new Object[]{roleID, codeUtilisateur})
                .build();
    }

    private ElementNotFoundException utilisateurNotFoundException(String codeUtilisateur,String roleID) {
        return ElementNotFoundException.builder()
                .key(CoreConstants.BusinessExceptionMessage.UTILISATEUR_NOT_FOUND)
                .args(new Object[]{roleID, codeUtilisateur})
                .build();
    }

    private ElementByCodeProfesseurResponse getElement(String codeProfesseur) throws ExchangerException{
        ElementByCodeProfesseurResponse elementResponse;
        try{
            elementResponse = elementClient.getElementsByCodeProfesseur(codeProfesseur).getBody();
        }catch (HttpClientErrorException | HttpServerErrorException e) {
            throw ExchangerException.builder()
                    .exceptionBody(e.getResponseBodyAsString())
                    .build();
        }
        return elementResponse;
    }

    private List<ElementByCodeProfesseurResponse> getElements(List<String> codesProfesseur) throws ExchangerException{
        List<ElementByCodeProfesseurResponse> elementResponses;
        try {
            elementResponses = elementClient.getElementsByCodeProfesseurs(codesProfesseur).getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw ExchangerException.builder()
                    .exceptionBody(e.getResponseBodyAsString())
                    .build();
        }
        return elementResponses;
    }

}
