package ma.enset.filiereservice.service;

import ma.enset.filiereservice.exception.ElementAlreadyExistsException;
import ma.enset.filiereservice.exception.ElementNotFoundException;
import ma.enset.filiereservice.exception.InternalErrorException;
import ma.enset.filiereservice.model.RegleDeCalcul;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RegleDeCalculService {
    RegleDeCalcul save(RegleDeCalcul module) throws ElementAlreadyExistsException, InternalErrorException;
    List<RegleDeCalcul> saveAll(List<RegleDeCalcul> modules) throws ElementAlreadyExistsException, InternalErrorException;
    RegleDeCalcul findByCodeRegleDeCalcul(String codeRegleDeCalcul) throws ElementNotFoundException;
    Page<RegleDeCalcul> findAll(Pageable pageable);
    RegleDeCalcul update(RegleDeCalcul module) throws ElementNotFoundException, InternalErrorException;
    void deleteByCodeRegleDeCalcul(String codeRegleDeCalcul) throws ElementNotFoundException;
    void deleteAllByCodeRegleDeCalcul(List<String> codesRegleDeCalculs) throws ElementNotFoundException;


}
