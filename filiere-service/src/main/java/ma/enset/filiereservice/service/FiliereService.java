package ma.enset.filiereservice.service;

import ma.enset.filiereservice.dto.*;
import ma.enset.filiereservice.exception.ElementAlreadyExistsException;
import ma.enset.filiereservice.exception.ElementNotFoundException;
import ma.enset.filiereservice.exception.InternalErrorException;
import ma.enset.filiereservice.model.Filiere;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface FiliereService {
    FiliereResponse save(FiliereCreationRequest filiere) throws ElementAlreadyExistsException;

    List<FiliereResponse> saveAll(List<FiliereCreationRequest> filieres) throws ElementAlreadyExistsException;

    FiliereResponse findById(String id, boolean includeSemestre, boolean includeRegleDeCalcule, boolean includeChefFiliere) throws ElementNotFoundException;

    List<FiliereResponse> findAllById(Set<String> ids, boolean includeSemestre, boolean includeRegleDeCalcule, boolean includeChefFiliere) throws ElementNotFoundException;

    FilierePagingResponse findAll(int page, int size, String search, boolean includeSemestre, boolean includeRegleDeCalcule, boolean includeChefFiliere);

    FiliereResponse update(String id, FiliereUpdateRequest filiere) throws ElementNotFoundException;

    void deleteById(String id) throws ElementNotFoundException;

    void deleteById(Set<String> ids) throws ElementNotFoundException;

    void deleteByCodeDepartement(String code) throws ElementNotFoundException;

    void deleteByCodeDepartement(Set<String> codes) throws ElementNotFoundException;

    FiliereByDepartementResponse findByCodeDepartement(String codeDepartement, boolean includeSemestre, boolean includeRegleDeCalcule, boolean includeChefFiliere) throws ElementNotFoundException;

    List<FiliereByDepartementResponse> findAllByCodeDepartement(Set<String> codeDepartement, boolean includeSemestre, boolean includeRegleDeCalcule, boolean includeChefFiliere) throws ElementNotFoundException;
}
