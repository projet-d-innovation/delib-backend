package ma.enset.utilisateur.service;

import ma.enset.utilisateur.exception.InternalErrorException;
import ma.enset.utilisateur.exception.ElementAlreadyExistsException;
import ma.enset.utilisateur.exception.ElementNotFoundException;
import ma.enset.utilisateur.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoleService {
    Role save(Role role) throws ElementAlreadyExistsException, InternalErrorException;

    List<Role> saveAll(List<Role> roles) throws ElementAlreadyExistsException, InternalErrorException;

    Role findByRoleId(String codeRole) throws ElementNotFoundException;

    List<Role> findAllByRoleId(List<String> codeRoles) throws ElementNotFoundException;

    Page<Role> findAll(Pageable pageable);

    Role update(Role role) throws ElementNotFoundException, InternalErrorException;

    List<Role> updateAll(List<Role> roles) throws ElementNotFoundException, InternalErrorException;

    void deleteByRoleId(String codeRole) throws ElementNotFoundException;

    void deleteAllByRoleId(List<String> codesRole) throws ElementNotFoundException;

}
