package ma.enset.filiereservice.service;

import ma.enset.filiereservice.exception.ElementAlreadyExistsException;
import ma.enset.filiereservice.exception.ElementNotFoundException;
import ma.enset.filiereservice.exception.InternalErrorException;
import ma.enset.filiereservice.model.Filiere;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface FiliereService {
    Filiere save(Filiere filiere) throws ElementAlreadyExistsException, InternalErrorException ,ElementNotFoundException;
    List<Filiere> saveAll(List<Filiere> filieres) throws ElementAlreadyExistsException, InternalErrorException, ElementNotFoundException;
    Filiere findByCodeFiliere(String codeFiliere) throws ElementNotFoundException;

    List<Filiere> findByCodeRegle(String codeRegle ) ;
    Filiere findByCodeChefFiliere(String codeChefFiliere)throws  ElementNotFoundException;

    List<Filiere> findByCodeDepartement(String codeDepartement ) ;


    Page<Filiere> findAll(Pageable pageable);
    void deleteByCodeFiliere(String codeFiliere) throws ElementNotFoundException , InternalErrorException;


    void deleteAllByCodeFiliere(List<String> codesFilieres) throws ElementNotFoundException , InternalErrorException;

    Filiere addSemestreToFiliere(String codeFiliere, String codeSemestre)throws ElementNotFoundException, InternalErrorException , ElementAlreadyExistsException;

    void deleteSemestreFromFiliere(String codeFiliere, String codeSemestre)  throws ElementNotFoundException, InternalErrorException;

    Filiere addAnneUnivToFiliere(String codeFiliere, String codeAnneUniv) throws ElementNotFoundException, InternalErrorException , ElementAlreadyExistsException ;

    void deleteAnneUnivFromFiliere(String codeFiliere, String codeAnneUniv) throws ElementNotFoundException, InternalErrorException;

    void existByCodeFiliere(String codeFiliere)  throws ElementNotFoundException;

    List<Filiere> findByCodeFiliereContaining(String codeFiliere) ;

    Filiere update(Filiere module) throws ElementNotFoundException, InternalErrorException;

    List<Filiere> retrieveAllFiliereByChef(String codeChefFiliere) throws ElementNotFoundException;
    boolean isThisUserAChef(String codeUser) ;


}
