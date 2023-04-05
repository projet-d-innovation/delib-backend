package ma.enset.utilisateur.service;

import ma.enset.utilisateur.exception.InternalErrorException;
import ma.enset.utilisateur.exception.RoleAlreadyExistsException;
import ma.enset.utilisateur.exception.RoleNotFoundException;
import ma.enset.utilisateur.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoleService {
    Role create(Role role) throws RoleAlreadyExistsException, InternalErrorException;

    List<Role> createMany(List<Role> roles) throws RoleAlreadyExistsException, InternalErrorException;

    Role findById(String codeRole) throws RoleNotFoundException;

    List<Role> findManyById(List<String> codeRoles) throws RoleNotFoundException;

    Page<Role> findAll(Pageable pageable);

    Role update(Role role) throws RoleNotFoundException, InternalErrorException;

    List<Role> updateMany(List<Role> roles) throws RoleNotFoundException, InternalErrorException;

    void deleteById(String codeRole) throws RoleNotFoundException;

    void deleteManyById(List<String> codesRole) throws RoleNotFoundException;

}
