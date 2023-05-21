package ma.enset.element.service;

import lombok.RequiredArgsConstructor;
import ma.enset.element.client.ModuleClient;
import ma.enset.element.client.UserClient;
import ma.enset.element.constant.CoreConstants;
import ma.enset.element.dto.*;
import ma.enset.element.exception.ApiClientException;
import ma.enset.element.exception.DuplicateEntryException;
import ma.enset.element.exception.ElementAlreadyExistsException;
import ma.enset.element.exception.ElementNotFoundException;
import ma.enset.element.exception.handler.dao.ExceptionResponse;
import ma.enset.element.mapper.ElementMapper;
import ma.enset.element.model.Element;
import ma.enset.element.repository.ElementRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ElementServiceImpl implements ElementService {
    private final String ELEMENT_TYPE = "Element";
    private final String ID_FIELD_NAME = "codeElement";
    private final ElementRepository repository;
    private final ElementMapper mapper;
    private final ModuleClient moduleClient;
    private final UserClient userClient;

    @Override
    public ElementResponse save(ElementCreationRequest request) throws ElementAlreadyExistsException,
                                                                        ApiClientException {

        moduleClient.modulesExist(Set.of(request.codeModule()));

        if (request.codeProfesseur() != null) {
            profExists(request.codeProfesseur());
        }

        Element element = mapper.toElement(request);

        try {
            return mapper.toElementResponse(repository.save(element));
        } catch (DataIntegrityViolationException e) {
            throw new ElementAlreadyExistsException(
                CoreConstants.BusinessExceptionMessage.ALREADY_EXISTS,
                new Object[] {ELEMENT_TYPE, ID_FIELD_NAME, request.codeElement()},
                null
            );
        }
    }

    @Override
    public List<ElementResponse> saveAll(List<ElementCreationRequest> request) throws ElementAlreadyExistsException,
                                                                                        DuplicateEntryException,
                                                                                        ApiClientException {
        int uniqueElementsCount = (int) request.stream()
                                                .map(ElementCreationRequest::codeElement)
                                                .distinct().count();

        if (uniqueElementsCount != request.size()) {
            throw new DuplicateEntryException(
                CoreConstants.BusinessExceptionMessage.DUPLICATE_ENTRY,
                new Object[]{ELEMENT_TYPE}
            );
        }

        List<Element> foundElements = repository.findAllById(
            request.stream()
                    .map(ElementCreationRequest::codeElement)
                    .collect(Collectors.toSet())
        );

        if (!foundElements.isEmpty()) {
            throw new ElementAlreadyExistsException(
                CoreConstants.BusinessExceptionMessage.MANY_ALREADY_EXISTS,
                new Object[]{ELEMENT_TYPE},
                foundElements.stream()
                                .map(Element::getCodeElement)
                                .toList()
            );
        }

        moduleClient.modulesExist(
            request.stream()
                    .map(ElementCreationRequest::codeModule)
                    .collect(Collectors.toSet())
        );

        Set<String> codesProfesseur = request.stream()
                                            .map(ElementCreationRequest::codeProfesseur)
                                            .collect(Collectors.toSet());

        if (!codesProfesseur.isEmpty()) {
            allProfsExist(codesProfesseur);
        }

        List<Element> elements = mapper.toElementList(request);

        return mapper.toElementResponseList(repository.saveAll(elements));
    }

    @Override
    public ElementResponse findById(String codeElement) throws ElementNotFoundException {
        return mapper.toElementResponse(
            repository.findById(codeElement).orElseThrow(() ->
                new ElementNotFoundException(
                    CoreConstants.BusinessExceptionMessage.NOT_FOUND,
                    new Object[] {ELEMENT_TYPE, ID_FIELD_NAME, codeElement},
                    null
                )
            )
        );
    }

    @Override
    public ElementPagingResponse findAll(int page, int size) {
        return mapper.toPagingResponse(repository.findAll(PageRequest.of(page, size)));
    }

    @Override
    public List<ElementResponse> findAllByIds(Set<String> codesElement) throws ElementNotFoundException {

        List<Element> foundElements = repository.findAllById(codesElement);

        if (codesElement.size() != foundElements.size()) {
            throw new ElementNotFoundException(
                CoreConstants.BusinessExceptionMessage.MANY_NOT_FOUND,
                new Object[] {ELEMENT_TYPE},
                codesElement.stream()
                            .filter(
                                codeElement -> !foundElements.stream()
                                                                .map(Element::getCodeElement)
                                                                .toList()
                                                                .contains(codeElement)
                            )
                            .toList()
            );
        }

        return mapper.toElementResponseList(foundElements);
    }

    @Override
    public List<ElementResponse> findAllByCodeModule(String codeModule) {
        return mapper.toElementResponseList(repository.findAllByCodeModule(codeModule));
    }

    @Override
    public List<GroupedElementsResponse> findAllByCodesModule(Set<String> codesModule) {

        return repository.findAllByCodeModuleIn(codesModule)
                            .stream()
                            .collect(Collectors.groupingBy(Element::getCodeModule))
                            .entrySet().stream()
                                        .map(entry ->
                                            GroupedElementsResponse.builder()
                                                .codeModule(entry.getKey())
                                                .elements(mapper.toElementResponseList(entry.getValue()))
                                                .build()
                                        )
                                        .toList();
    }

    @Override
    public List<ElementResponse> findAllByCodeProfesseur(String codeProfesseur) {
        return mapper.toElementResponseList(repository.findAllByCodeProfesseur(codeProfesseur));
    }

    @Override
    public List<GroupedElementsResponse> findAllByCodesProfesseur(Set<String> codesProfesseur) {

        return repository.findAllByCodeProfesseurIn(codesProfesseur)
                            .stream()
                            .collect(Collectors.groupingBy(Element::getCodeProfesseur))
                            .entrySet().stream()
                                        .map(entry ->
                                            GroupedElementsResponse.builder()
                                                .codeProfesseur(entry.getKey())
                                                .elements(mapper.toElementResponseList(entry.getValue()))
                                                .build()
                                        )
                                        .toList();
    }

    @Override
    public void existAllByIds(Set<String> codesElement) throws ElementNotFoundException {

        List<String> foundElementsCodes = repository.findAllById(codesElement)
                                                    .stream().map(Element::getCodeElement).toList();

        if (codesElement.size() != foundElementsCodes.size()) {
            throw new ElementNotFoundException(
                CoreConstants.BusinessExceptionMessage.MANY_NOT_FOUND,
                new Object[] {ELEMENT_TYPE},
                codesElement.stream()
                            .filter(code -> !foundElementsCodes.contains(code))
                            .toList()
            );
        }
    }

    @Override
    public ElementResponse update(String codeElement, ElementUpdateRequest request) throws ElementNotFoundException,
                                                                                            ApiClientException {

        Element element = repository.findById(codeElement).orElseThrow(() ->
            new ElementNotFoundException(
                CoreConstants.BusinessExceptionMessage.NOT_FOUND,
                new Object[] {ELEMENT_TYPE, ID_FIELD_NAME, codeElement},
                null
            )
        );

        String oldCodeProfesseur = element.getCodeProfesseur();

        mapper.updateElementFromDTO(request, element);

        if (!Objects.equals(oldCodeProfesseur, element.getCodeProfesseur()) &&
                                                            element.getCodeProfesseur() != null) {
            profExists(element.getCodeProfesseur());
        }

        return mapper.toElementResponse(repository.save(element));
    }

    @Override
    public void deleteById(String codeElement) throws ElementNotFoundException {

        if (!repository.existsById(codeElement)) {
            throw new ElementNotFoundException(
                CoreConstants.BusinessExceptionMessage.NOT_FOUND,
                new Object[] {ELEMENT_TYPE, ID_FIELD_NAME, codeElement},
                null
            );
        }

        repository.deleteById(codeElement);
    }

    @Override
    public void deleteAllByIds(Set<String> codesElement) throws ElementNotFoundException {

        existAllByIds(codesElement);

        repository.deleteAllById(codesElement);
    }

    @Override
    public void deleteAllByCodeModule(String codeModule) {
        repository.deleteAllByCodeModule(codeModule);
    }

    @Override
    public void deleteAllByCodesModule(Set<String> codesModule) {
        repository.deleteAllByCodeModuleIn(codesModule);
    }

    private void profExists(String codeProfesseur) throws ApiClientException {
        try {
            userClient.profExits(codeProfesseur);
        } catch (ApiClientException e) {
            injectCustomProfMessage(e.getException());
            throw new ApiClientException(e.getException());
        }
    }

    private void allProfsExist(Set<String> codesProfesseur) throws ApiClientException {
        try {
            userClient.allProfsExit(codesProfesseur);
        } catch (ApiClientException e) {
            injectCustomProfMessage(e.getException());
            throw new ApiClientException(e.getException());
        }
    }

    private void injectCustomProfMessage(ExceptionResponse response) {
        String customMessage = response.getMessage()
                                        .replaceAll("(Utilisateur|User)", "Professeur");

        response.setMessage(customMessage);
    }
}
