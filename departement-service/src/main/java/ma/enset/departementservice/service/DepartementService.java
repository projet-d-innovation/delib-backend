package ma.enset.departementservice.service;

import ma.enset.departementservice.exception.ElementAlreadyExistsException;
import ma.enset.departementservice.exception.ElementNotFoundException;
import ma.enset.departementservice.exception.InternalErrorException;
import ma.enset.departementservice.model.Departement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DepartementService {
    Departement save(Departement module) throws ElementAlreadyExistsException, InternalErrorException;

    List<Departement> saveAll(List<Departement> modules) throws ElementAlreadyExistsException, InternalErrorException;

    Departement findByCodeDepartement(String codeDepartement) throws ElementNotFoundException;

    Page<Departement> findAll(Pageable pageable);

    Departement update(Departement module) throws ElementNotFoundException, InternalErrorException;

    void deleteByCodeDepartement(String codeDepartement) throws ElementNotFoundException , InternalErrorException;
    void deleteAllByCodeDepartement(List<String> codesDepartements) throws ElementNotFoundException , InternalErrorException;


    Departement pushFiliereToDepartment(String codeDepartement, String codeFiliere) throws InternalErrorException, ElementNotFoundException ;

    void deleteFiliereFromDepartment(String codeFiliere , String codeDepartement) throws InternalErrorException, ElementNotFoundException  ;

    Departement pushUserToDepartment(String codeDepartement, String codeUser) throws InternalErrorException, ElementNotFoundException ;

    void pushUsersToDepartment(String codeDepartement, List<String> codeUsers) throws InternalErrorException, ElementNotFoundException ;

    void deleteUserFromDepartment(String codeUser, String codeDepartement) throws InternalErrorException, ElementNotFoundException  ;

    void isUserInDepartment(String codeUser, String codeDepartement) throws ElementNotFoundException ;
    List<Departement> findByCodeDepartementContaining(String codeDepartement);

    void existByCodeDepartement(String codeDepartement) ;

}
