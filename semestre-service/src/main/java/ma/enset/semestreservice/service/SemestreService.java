package ma.enset.semestreservice.service;

import ma.enset.semestreservice.dto.*;
import ma.enset.semestreservice.exception.DuplicateEntryException;
import ma.enset.semestreservice.exception.ElementAlreadyExistsException;
import ma.enset.semestreservice.exception.ElementNotFoundException;

import java.util.List;
import java.util.Set;

public interface SemestreService {
    SemestreResponse save(SemestreCreationRequest request) throws ElementAlreadyExistsException;

    List<SemestreResponse> saveAll(List<SemestreCreationRequest> request) throws ElementAlreadyExistsException, DuplicateEntryException;

    boolean existAllByIds(Set<String> codesSemestre) throws ElementNotFoundException;

    SemestreResponse findById(String codeSemestre, boolean includeModules) throws ElementNotFoundException;

    List<SemestreResponse> findAllByIds(Set<String> codesSemestre, boolean includeModules) throws ElementNotFoundException;

    SemestrePagingResponse findAll(int page, int size, boolean includeModules);

    List<SemestreResponse> findAllByCodeFiliere(String codeFiliere);

    List<GroupedSemestresResponse> findAllByCodesFiliere(Set<String> codesFiliere);

    SemestreResponse update(String codeSemestre, SemestreUpdateRequest request) throws ElementNotFoundException;

    void deleteById(String codeSemestre) throws ElementNotFoundException;

    void deleteAllByIds(Set<String> codesSemestre);

    void deleteAllByCodeFiliere(String CodeFiliere);

    void deleteAllByCodesFiliere(Set<String> CodesFiliere);
}
