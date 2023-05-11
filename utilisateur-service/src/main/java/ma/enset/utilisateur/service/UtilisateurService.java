package ma.enset.utilisateur.service;

import ma.enset.utilisateur.exception.*;
import ma.enset.utilisateur.model.Utilisateur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UtilisateurService {
    Utilisateur save(Utilisateur utilisateur, String roleId) throws ElementAlreadyExistsException, InternalErrorException;

    Utilisateur save(Utilisateur utilisateur) throws ElementAlreadyExistsException, InternalErrorException;

    List<Utilisateur> saveAll(List<Utilisateur> utilisateur, String roleId) throws ElementAlreadyExistsException, InternalErrorException;

    List<Utilisateur> saveAll(List<Utilisateur> utilisateurs) throws ElementAlreadyExistsException, InternalErrorException;

    Utilisateur findByCodeUtilisateur(String codeUtilisateur, String roleId) throws ElementNotFoundException;

    Utilisateur findByCodeUtilisateur(String codeUtilisateur) throws ElementNotFoundException;

    List<Utilisateur> findAllByCodeUtilisateur(List<String> codeUtilisateurs, String roleId) throws ElementNotFoundException;

    List<Utilisateur> findAllByCodeUtilisateur(List<String> codeUtilisateurs) throws ElementNotFoundException;

    Page<Utilisateur> findAll(String search,Pageable pageable);

    Page<Utilisateur> findAll(String search,Pageable pageable, String roleId) throws ElementNotFoundException;

    Utilisateur update(Utilisateur utilisateur, String roleId) throws ElementNotFoundException, InternalErrorException;

    Utilisateur update(Utilisateur utilisateur) throws ElementNotFoundException, InternalErrorException;

    List<Utilisateur> updateAll(List<Utilisateur> utilisateurs, String roleId) throws ElementNotFoundException, InternalErrorException;

    List<Utilisateur> updateAll(List<Utilisateur> utilisateurs) throws ElementNotFoundException, InternalErrorException;

    void deleteByCodeUtilisateur(String codeUtilisateur, String roleId) throws ElementNotFoundException;

    void deleteByCodeUtilisateur(String codeUtilisateur) throws ElementNotFoundException;

    void deleteAllByCodeUtilisateur(List<String> codesUtilisateur, String roleId) throws ElementNotFoundException;

    void deleteAllByCodeUtilisateur(List<String> codesUtilisateur) throws ElementNotFoundException;

}
