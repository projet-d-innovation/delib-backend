package ma.enset.utilisateur.service;

import ma.enset.utilisateur.exception.*;
import ma.enset.utilisateur.model.Utilisateur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UtilisateurService {
    Utilisateur create(Utilisateur utilisateur, String roleId) throws UtilisateurAlreadyExistsException, InternalErrorException, RoleNotFoundException;

    Utilisateur create(Utilisateur utilisateur) throws UtilisateurAlreadyExistsException, InternalErrorException, RoleNotFoundException;

    List<Utilisateur> createMany(List<Utilisateur> utilisateur, String roleId) throws UtilisateurAlreadyExistsException, InternalErrorException, RoleNotFoundException;

    List<Utilisateur> createMany(List<Utilisateur> utilisateurs) throws UtilisateurAlreadyExistsException, InternalErrorException, RoleNotFoundException;

    Utilisateur findById(String codeUtilisateur, String roleId) throws UtilisateurNotFoundException;

    Utilisateur findById(String codeUtilisateur) throws UtilisateurNotFoundException;

    List<Utilisateur> findManyById(List<String> codeUtilisateurs, String roleId) throws UtilisateurNotFoundException;

    List<Utilisateur> findManyById(List<String> codeUtilisateurs) throws UtilisateurNotFoundException;

    Page<Utilisateur> findAll(Pageable pageable);

    Page<Utilisateur> findAll(Pageable pageable, String roleId);

    Utilisateur update(Utilisateur utilisateur, String roleId) throws UtilisateurNotFoundException, RoleConflictException, InternalErrorException;

    Utilisateur update(Utilisateur utilisateur) throws UtilisateurNotFoundException, RoleConflictException, InternalErrorException;

    List<Utilisateur> updateMany(List<Utilisateur> utilisateurs, String roleId) throws UtilisateurNotFoundException, RoleConflictException, InternalErrorException;

    List<Utilisateur> updateMany(List<Utilisateur> utilisateurs) throws UtilisateurNotFoundException, RoleConflictException, InternalErrorException;

    void deleteById(String codeUtilisateur, String roleId) throws UtilisateurNotFoundException;

    void deleteById(String codeUtilisateur) throws UtilisateurNotFoundException;

    void deleteManyById(List<String> codesUtilisateur, String roleId) throws UtilisateurNotFoundException;

    void deleteManyById(List<String> codesUtilisateur) throws UtilisateurNotFoundException;

}
