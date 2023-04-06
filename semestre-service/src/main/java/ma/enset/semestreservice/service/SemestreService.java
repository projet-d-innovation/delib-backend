package ma.enset.semestreservice.service;


import ma.enset.semestreservice.exception.BusinessException;
import ma.enset.semestreservice.exception.FiliereNotFoundException;
import ma.enset.semestreservice.model.Semestre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SemestreService {

    /// It can throw many exceptions ,
    // so that's why we use the BusinessException ,
    // we don't know which exception will be thrown in the future
    //but all exceptions will inherit from the BusinessException
    Semestre create(Semestre element) throws BusinessException;

    List<Semestre> createMany(List<Semestre> elements) throws BusinessException;

    Semestre findById(String code) throws BusinessException;

    List<Semestre> findManyById(List<String> codes) throws BusinessException;

    Page<Semestre> findAll(Pageable pageable);

    Semestre update(Semestre element) throws BusinessException;

    void deleteById(String code) throws BusinessException;

    void deleteManyById(List<String> codes) throws BusinessException;

    public Boolean existsByCodeFiliere(String codeDepartement) throws FiliereNotFoundException;

}
