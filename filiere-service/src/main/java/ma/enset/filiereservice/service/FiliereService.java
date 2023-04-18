package ma.enset.filiereservice.service;

import ma.enset.filiereservice.exception.ElementAlreadyExistsException;
import ma.enset.filiereservice.exception.ElementNotFoundException;
import ma.enset.filiereservice.exception.InternalErrorException;
import ma.enset.filiereservice.model.Filiere;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FiliereService {
    Filiere save(Filiere filiere) throws ElementAlreadyExistsException, InternalErrorException;
    List<Filiere> saveAll(List<Filiere> filieres) throws ElementAlreadyExistsException, InternalErrorException;
    Filiere findByCodeFiliere(String codeFiliere) throws ElementNotFoundException;

    List<Filiere> findByCodeRegle(String codeRegle ) ;
    Filiere findByCodeChefFiliere(String codeChefFiliere) ;

    List<Filiere> findByCodeDepartement(String codeDepartement ) ;


    Page<Filiere> findAll(Pageable pageable);
    Filiere update(Filiere module) throws ElementNotFoundException, InternalErrorException;
    void deleteByCodeFiliere(String codeFiliere) throws ElementNotFoundException;


    void deleteAllByCodeFiliere(List<String> codesFilieres) throws ElementNotFoundException;

}
