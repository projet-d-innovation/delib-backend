package ma.enset.element.service;

import ma.enset.element.dto.*;
import ma.enset.element.exception.DuplicateEntryException;
import ma.enset.element.exception.ElementAlreadyExistsException;
import ma.enset.element.exception.ElementNotFoundException;

import java.util.List;
import java.util.Set;

public interface ElementService {
    ElementResponse save(ElementCreationRequest request) throws ElementAlreadyExistsException;

    List<ElementResponse> saveAll(List<ElementCreationRequest> request) throws ElementAlreadyExistsException,
        DuplicateEntryException;

    ElementResponse findById(String codeElement) throws ElementNotFoundException;

    ElementPagingResponse findAll(int page, int size);

    List<ElementResponse> findAllByIds(Set<String> codesElement) throws ElementNotFoundException;

    List<ElementResponse> findModuleElements(String codeModule);

    List<ModuleElementResponse> findAllModulesElements(Set<String> codesModule);

    List<ElementResponse> findProfesseurElements(String codeProfesseur);

    List<ProfesseurElementsResponse> findAllProfesseursElements(Set<String> codesProfesseur);

    boolean existAllByIds(Set<String> codesElement) throws ElementNotFoundException;

    ElementResponse update(String codeElement, ElementUpdateRequest request) throws ElementNotFoundException;

    void deleteById(String codeElement) throws ElementNotFoundException;

    void deleteAllByIds(Set<String> codesElement) throws ElementNotFoundException;

    void deleteModuleElements(String codeModule);

    void deleteAllModulesElements(Set<String> codesModule);
}