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
    void deleteByCodeDepartement(String codeDepartement) throws ElementNotFoundException;
    void deleteAllByCodeDepartement(List<String> codesDepartements) throws ElementNotFoundException;


}
