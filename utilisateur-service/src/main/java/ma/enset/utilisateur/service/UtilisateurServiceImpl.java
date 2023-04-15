package ma.enset.utilisateur.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.utilisateur.constant.CoreConstants;
import ma.enset.utilisateur.exception.ElementAlreadyExistsException;
import ma.enset.utilisateur.exception.ElementNotFoundException;
import ma.enset.utilisateur.exception.InternalErrorException;
import ma.enset.utilisateur.exception.RoleConflictException;
import ma.enset.utilisateur.model.Role;
import ma.enset.utilisateur.model.Utilisateur;
import ma.enset.utilisateur.repository.UtilisateurRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class UtilisateurServiceImpl implements UtilisateurService {
    private final UtilisateurRepository utilisateurRepository;
    private final RoleService roleService;


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
        return utilisateurRepository.findAllByRolesContains(role, pageable);
    }


    @Override
    public Utilisateur update(Utilisateur utilisateur, String roleId) throws ElementNotFoundException, RoleConflictException, InternalErrorException {

        Utilisateur toBeUpdated = excludeNullValue(
                this.getUtilisateurAndCheckRole(utilisateur.getCode(), roleId),
                utilisateur
        );

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

        Utilisateur toBeUpdated = excludeNullValue(
                this.getUtilisateur(utilisateur.getCode()),
                utilisateur
        );

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

        Role role = Role.builder()
                .roleId(roleId)
                .build();

        return utilisateurRepository.findByCodeAndRolesContains(codeUtilisateur, role)
                .orElseThrow(() ->
                        ElementNotFoundException.builder()
                                .key(CoreConstants.BusinessExceptionMessage.UTILISATEUR_NOT_FOUND)
                                .args(new Object[]{roleId, codeUtilisateur})
                                .build()
                );
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

        return utilisateur;
    }

    private Utilisateur excludeNullValue(Utilisateur utilisateur, Utilisateur toBeUpdated) {
        if (utilisateur.getCin() != null)
            toBeUpdated.setCin(utilisateur.getCin());
        if (utilisateur.getCne() != null)
            toBeUpdated.setCne(utilisateur.getCne());
        if (utilisateur.getNom() != null)
            toBeUpdated.setNom(utilisateur.getNom());
        if (utilisateur.getPrenom() != null)
            toBeUpdated.setPrenom(utilisateur.getPrenom());
        if (utilisateur.getAdresse() != null)
            toBeUpdated.setAdresse(utilisateur.getAdresse());
        if (utilisateur.getTelephone() != null)
            toBeUpdated.setTelephone(utilisateur.getTelephone());
        if (utilisateur.getPhoto() != null)
            toBeUpdated.setPhoto(utilisateur.getPhoto());
        if (utilisateur.getDateNaissance() != null)
            toBeUpdated.setDateNaissance(utilisateur.getDateNaissance());
        if (utilisateur.getVille() != null)
            toBeUpdated.setVille(utilisateur.getVille());
        if (utilisateur.getPays() != null)
            toBeUpdated.setPays(utilisateur.getPays());
        if (utilisateur.getRoles() != null)
            toBeUpdated.setRoles(utilisateur.getRoles());
        if (utilisateur.getCodeDepartement() != null)
            toBeUpdated.setCodeDepartement(utilisateur.getCodeDepartement());
        if (utilisateur.getElementIds() != null)
            toBeUpdated.setElementIds(utilisateur.getElementIds());

        return toBeUpdated;
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


}
