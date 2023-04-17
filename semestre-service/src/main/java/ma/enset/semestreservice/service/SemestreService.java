package ma.enset.semestreservice.service;


import ma.enset.semestreservice.exception.ElementAlreadyExistsException;
import ma.enset.semestreservice.exception.ElementNotFoundException;
import ma.enset.semestreservice.exception.InternalErrorException;
import ma.enset.semestreservice.model.Semestre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SemestreService {

    Semestre save(Semestre module) throws ElementAlreadyExistsException, InternalErrorException;
    List<Semestre> saveAll(List<Semestre> modules) throws ElementAlreadyExistsException, InternalErrorException;
    Semestre findByCodeSemestre(String codeSemestre) throws ElementNotFoundException;
    Page<Semestre> findAll(Pageable pageable);
    Semestre update(Semestre module) throws ElementNotFoundException, InternalErrorException;
    void deleteByCodeSemestre(String codeSemestre) throws ElementNotFoundException;
    void deleteAllByCodeSemestre(List<String> codesSemestres) throws ElementNotFoundException;

}
