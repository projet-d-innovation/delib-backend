package ma.enset.moduleservice.service;

import ma.enset.moduleservice.dto.ModuleCreationRequest;
import ma.enset.moduleservice.dto.ModulePagingResponse;
import ma.enset.moduleservice.dto.ModuleResponse;
import ma.enset.moduleservice.dto.ModuleUpdateRequest;
import ma.enset.moduleservice.exception.DuplicateEntryException;
import ma.enset.moduleservice.exception.ElementAlreadyExistsException;
import ma.enset.moduleservice.exception.ElementNotFoundException;

import java.util.List;
import java.util.Set;

public interface ModuleService {
    ModuleResponse save(ModuleCreationRequest request) throws ElementAlreadyExistsException;

    List<ModuleResponse> saveAll(List<ModuleCreationRequest> request) throws ElementAlreadyExistsException, DuplicateEntryException;

    ModuleResponse findById(String codeModule, boolean includeElements) throws ElementNotFoundException;

    ModulePagingResponse findAll(int page, int size, boolean includeElements);

    boolean existsAllId(Set<String> codesModule) throws ElementNotFoundException;

    ModuleResponse update(String codeModule, ModuleUpdateRequest request) throws ElementNotFoundException;

    void deleteById(String codeModule) throws ElementNotFoundException;

    void deleteByCodeSemestre(String codeSemestre) throws ElementNotFoundException;

    void deleteAllById(Set<String> codesModule) throws ElementNotFoundException;
}
