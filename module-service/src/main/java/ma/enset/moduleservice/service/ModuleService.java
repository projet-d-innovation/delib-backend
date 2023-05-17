package ma.enset.moduleservice.service;

import ma.enset.moduleservice.dto.*;
import ma.enset.moduleservice.exception.DuplicateEntryException;
import ma.enset.moduleservice.exception.ElementAlreadyExistsException;
import ma.enset.moduleservice.exception.ElementNotFoundException;

import java.util.List;
import java.util.Set;

public interface ModuleService {
    ModuleResponse save(ModuleCreationRequest request) throws ElementAlreadyExistsException;

    List<ModuleResponse> saveAll(List<ModuleCreationRequest> request) throws ElementAlreadyExistsException, DuplicateEntryException;

    boolean existAllByIds(Set<String> codesModule) throws ElementNotFoundException;

    ModuleResponse findById(String codeModule, boolean includeElements) throws ElementNotFoundException;

    List<ModuleResponse> findAllByIds(Set<String> codesModule, boolean includeElements) throws ElementNotFoundException;

    ModulePagingResponse findAll(int page, int size, boolean includeElements);

    List<ModuleResponse> findAllByCodeSemestre(String codeSemestre);

    List<GroupedModulesResponse> findAllByCodesSemestre(Set<String> codesSemestre);

    ModuleResponse update(String codeModule, ModuleUpdateRequest request) throws ElementNotFoundException;

    void deleteById(String codeModule) throws ElementNotFoundException;

    void deleteAllByIds(Set<String> codesModule);

    void deleteAllByCodeSemestre(String codeSemestre);

    void deleteAllByCodesSemestre(Set<String> codesSemestre);
}
