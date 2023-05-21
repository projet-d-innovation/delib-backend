package ma.enset.element.service;

import ma.enset.element.dto.*;
import ma.enset.element.exception.ApiClientException;
import ma.enset.element.exception.DuplicateEntryException;
import ma.enset.element.exception.ElementAlreadyExistsException;
import ma.enset.element.exception.ElementNotFoundException;

import java.util.List;
import java.util.Set;

public interface ElementService {
    ElementResponse save(ElementCreationRequest request) throws ElementAlreadyExistsException, ApiClientException;

    List<ElementResponse> saveAll(List<ElementCreationRequest> request) throws ElementAlreadyExistsException,
                                                                                DuplicateEntryException, ApiClientException;

    ElementResponse findById(String codeElement) throws ElementNotFoundException;

    ElementPagingResponse findAll(int page, int size);

    List<ElementResponse> findAllByIds(Set<String> codesElement) throws ElementNotFoundException;

    List<ElementResponse> findAllByCodeModule(String codeModule);

    List<GroupedElementsResponse> findAllByCodesModule(Set<String> codesModule);

    List<ElementResponse> findAllByCodeProfesseur(String codeProfesseur);

    List<GroupedElementsResponse> findAllByCodesProfesseur(Set<String> codesProfesseur);

    void existAllByIds(Set<String> codesElement) throws ElementNotFoundException;

    ElementResponse update(String codeElement, ElementUpdateRequest request) throws ElementNotFoundException, ApiClientException;

    void deleteById(String codeElement) throws ElementNotFoundException;

    void deleteAllByIds(Set<String> codesElement) throws ElementNotFoundException;

    void deleteAllByCodeModule(String codeModule);

    void deleteAllByCodesModule(Set<String> codesModule);
}