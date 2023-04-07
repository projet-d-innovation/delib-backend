package ma.enset.utilisateur.service;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import ma.enset.utilisateur.constant.CoreConstants;
import ma.enset.utilisateur.exception.InternalErrorException;
import ma.enset.utilisateur.exception.RoleConflictException;
import ma.enset.utilisateur.exception.UtilisateurAlreadyExistsException;
import ma.enset.utilisateur.exception.UtilisateurNotFoundException;
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
public class UtilisateurServiceImpl implements UtilisateurService {
    private final UtilisateurRepository utilisateurRepository;
    private final RoleService roleService;


    @Override
    public Utilisateur create(Utilisateur utilisateur, String roleId) throws UtilisateurAlreadyExistsException, InternalErrorException {
        Utilisateur createdUtilisateur = null;
        if (utilisateurRepository.existsById(utilisateur.getCode()))
            throw UtilisateurAlreadyExistsException.builder()
                    .key(CoreConstants.BusinessExceptionMessage.UTILISATEUR_ALREADY_EXISTS)
                    .args(new Object[]{roleId, "code", utilisateur.getCode()})
                    .build();

        Role role = roleService.findById(roleId);
        utilisateur.setRoles(List.of(role));

        try {
            createdUtilisateur = utilisateurRepository.save(utilisateur);
        } catch (Exception e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                throw InternalErrorException.builder()
                        .key(CoreConstants.BusinessExceptionMessage.INTERNAL_ERROR)
                        .build();
            }
        }

        return createdUtilisateur;
    }

    @Override
    public Utilisateur create(Utilisateur utilisateur) throws UtilisateurAlreadyExistsException, InternalErrorException {
        Utilisateur createdUtilisateur = null;
        if (utilisateurRepository.existsById(utilisateur.getCode()))
            throw UtilisateurAlreadyExistsException.builder()
                    .key(CoreConstants.BusinessExceptionMessage.UTILISATEUR_ALREADY_EXISTS)
                    .args(new Object[]{"Utilisateur", "code", utilisateur.getCode()})
                    .build();

        for (Role role : utilisateur.getRoles()) {
            role = roleService.findById(role.getRoleId());
        }

        try {
            createdUtilisateur = utilisateurRepository.save(utilisateur);
        } catch (Exception e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                throw InternalErrorException.builder()
                        .key(CoreConstants.BusinessExceptionMessage.INTERNAL_ERROR)
                        .build();
            }
        }

        return createdUtilisateur;
    }

    @Transactional
    @Override
    public List<Utilisateur> createMany(List<Utilisateur> utilisateurs, String roleId) throws UtilisateurAlreadyExistsException, InternalErrorException {
        List<Utilisateur> createdUtilisateurs = new ArrayList<>();
        Role role = roleService.findById(roleId);
        for (Utilisateur utilisateur : utilisateurs) {
            utilisateur.setRoles(List.of(role));
        }

        try {
            createdUtilisateurs = this.createMany(utilisateurs);
        } catch (Exception e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                throw InternalErrorException.builder()
                        .key(CoreConstants.BusinessExceptionMessage.INTERNAL_ERROR)
                        .build();
            }
        }

        return createdUtilisateurs;
    }

    @Transactional
    @Override
    public List<Utilisateur> createMany(List<Utilisateur> utilisateurs) throws UtilisateurAlreadyExistsException, InternalErrorException {

        List<Utilisateur> createdUtilisateurs = new ArrayList<>();

        for (Utilisateur utilisateur : utilisateurs) {
            if (utilisateurRepository.existsById(utilisateur.getCode()))
                throw UtilisateurAlreadyExistsException.builder()
                        .key(CoreConstants.BusinessExceptionMessage.UTILISATEUR_ALREADY_EXISTS)
                        .args(new Object[]{utilisateur.getRoles()
                                .get(0)
                                .getRoleId(), "code", utilisateur.getCode()})
                        .build();
        }

        try {
            createdUtilisateurs = utilisateurRepository.saveAll(utilisateurs);
        } catch (Exception e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                throw InternalErrorException.builder()
                        .key(CoreConstants.BusinessExceptionMessage.INTERNAL_ERROR)
                        .build();
            }
        }

        return createdUtilisateurs;
    }

    @Override
    public Page<Utilisateur> findAll(Pageable pageable) {
        return utilisateurRepository.findAll(pageable);
    }

    @Override
    public Page<Utilisateur> findAll(Pageable pageable, String roleId) {
        Role role = Role.builder()
                .roleId(roleId)
                .build();
        return utilisateurRepository.findAllByRolesContains(role, pageable);
    }


    @Override
    public Utilisateur update(Utilisateur utilisateur, String roleId) throws UtilisateurNotFoundException, RoleConflictException, InternalErrorException {

        Utilisateur toBeUpdated = excludeNullValue(
                this.getUtilisateurAndCheckRole(utilisateur.getCode(), roleId),
                utilisateur
        );

        Utilisateur updatedUtilisateur = null;

        try {
            updatedUtilisateur = utilisateurRepository.save(toBeUpdated);
        } catch (Exception e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                throw InternalErrorException.builder()
                        .key(CoreConstants.BusinessExceptionMessage.INTERNAL_ERROR)
                        .build();
            }
        }

        return updatedUtilisateur;
    }

    @Override
    public Utilisateur update(Utilisateur utilisateur) throws UtilisateurNotFoundException, RoleConflictException, InternalErrorException {

        Utilisateur toBeUpdated = excludeNullValue(
                this.getUtilisateur(utilisateur.getCode()),
                utilisateur
        );

        Utilisateur updatedUtilisateur = null;

        try {
            updatedUtilisateur = utilisateurRepository.save(toBeUpdated);
        } catch (Exception e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                throw InternalErrorException.builder()
                        .key(CoreConstants.BusinessExceptionMessage.INTERNAL_ERROR)
                        .build();
            }
        }

        return updatedUtilisateur;
    }
    @Transactional
    @Override
    public List<Utilisateur> updateMany(List<Utilisateur> utilisateurs, String roleId) throws UtilisateurNotFoundException, RoleConflictException, InternalErrorException {
        List<Utilisateur> updatedUtilisateurs = new ArrayList<>();
        for (Utilisateur utilisateur : utilisateurs) {
            updatedUtilisateurs.add(this.update(utilisateur, roleId));
        }
        return updatedUtilisateurs;
    }

    @Transactional
    @Override
    public List<Utilisateur> updateMany(List<Utilisateur> utilisateurs) throws UtilisateurNotFoundException, RoleConflictException, InternalErrorException {
        List<Utilisateur> updatedUtilisateurs = new ArrayList<>();
        for (Utilisateur utilisateur : utilisateurs) {
            updatedUtilisateurs.add(this.update(utilisateur));
        }
        return updatedUtilisateurs;
    }


    @Override
    public void deleteById(String codeUtilisateur, String roleId) throws UtilisateurNotFoundException {
        this.getUtilisateurAndCheckRole(codeUtilisateur, roleId);
        utilisateurRepository.deleteById(codeUtilisateur);
    }

    @Override
    public void deleteById(String codeUtilisateur) throws UtilisateurNotFoundException {
        this.getUtilisateur(codeUtilisateur);
        utilisateurRepository.deleteById(codeUtilisateur);
    }

    @Transactional
    @Override
    public void deleteManyById(List<String> codeUtilisateurs, String roleId) throws UtilisateurNotFoundException {
        for (String codeUtilisateur : codeUtilisateurs) {
            this.deleteById(codeUtilisateur, roleId);
        }
    }

    @Transactional
    @Override
    public void deleteManyById(List<String> codeUtilisateurs) throws UtilisateurNotFoundException {
        for (String codeUtilisateur : codeUtilisateurs) {
            this.deleteById(codeUtilisateur);
        }
    }

    @Override
    public Utilisateur findById(String codeUtilisateur, String roleId) throws UtilisateurNotFoundException {

        Role role = Role.builder()
                .roleId(roleId)
                .build();

        return utilisateurRepository.findByCodeAndRolesContains(codeUtilisateur, role)
                .orElseThrow(() ->
                        UtilisateurNotFoundException.builder()
                                .key(CoreConstants.BusinessExceptionMessage.UTILISATEUR_NOT_FOUND)
                                .args(new Object[]{roleId, "code", codeUtilisateur})
                                .build()
                );
    }

    @Override
    public Utilisateur findById(String codeUtilisateur) throws UtilisateurNotFoundException {
        return utilisateurRepository.findById(codeUtilisateur)
                .orElseThrow(() ->
                        UtilisateurNotFoundException.builder()
                                .key(CoreConstants.BusinessExceptionMessage.UTILISATEUR_NOT_FOUND)
                                .args(new Object[]{"Utilisateur", "code", codeUtilisateur})
                                .build()
                );
    }


    @Override
    public List<Utilisateur> findManyById(List<String> codeUtilisateurs, String roleId) throws UtilisateurNotFoundException {
        List<Utilisateur> elements = new ArrayList<>();
        for (String codeUtilisateur : codeUtilisateurs) {
            elements.add(findById(codeUtilisateur, roleId));
        }
        return elements;
    }


    @Override
    public List<Utilisateur> findManyById(List<String> codeUtilisateurs) throws UtilisateurNotFoundException {
        List<Utilisateur> elements = new ArrayList<>();
        for (String codeUtilisateur : codeUtilisateurs) {
            elements.add(findById(codeUtilisateur));
        }
        return elements;
    }


    private Utilisateur getUtilisateur(String codeUtilisateur) throws UtilisateurNotFoundException {

        Utilisateur utilisateur = utilisateurRepository.findById(codeUtilisateur).orElse(null);

        if (utilisateur == null)
            throw UtilisateurNotFoundException.builder()
                    .key(CoreConstants.BusinessExceptionMessage.UTILISATEUR_NOT_FOUND)
                    .args(new Object[]{"Utilisateur", "code", codeUtilisateur})
                    .build();

        return utilisateur;
    }

    private Utilisateur getUtilisateurAndCheckRole(String codeUtlisateur, String roleId) throws UtilisateurNotFoundException, RoleConflictException {

        Utilisateur utilisateur = getUtilisateur(codeUtlisateur);

        if (utilisateur.getRoles().stream().map(Role::getRoleId).noneMatch(roleName -> roleName.equals(roleId)))
            throw RoleConflictException.builder()
                    .key(CoreConstants.BusinessExceptionMessage.ROLE_CONFLICT)
                    .args(new Object[]{"code", codeUtlisateur, roleId})
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


}
