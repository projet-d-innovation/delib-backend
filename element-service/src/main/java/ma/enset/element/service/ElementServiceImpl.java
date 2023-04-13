package ma.enset.element.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.element.constant.CoreConstants;
import ma.enset.element.exception.ElementAlreadyExistsException;
import ma.enset.element.exception.ElementNotFoundException;
import ma.enset.element.exception.InternalErrorException;
import ma.enset.element.model.Element;
import ma.enset.element.repository.ElementRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ElementServiceImpl implements ElementService {
    private final ElementRepository elementRepository;

    @Override
    public Element save(Element element) throws ElementAlreadyExistsException, InternalErrorException {

        if (elementRepository.existsByCodeElement(element.getCodeElement()))
            throw ElementAlreadyExistsException.builder()
                    .key(CoreConstants.BusinessExceptionMessage.ELEMENT_ALREADY_EXISTS)
                    .args(new Object[]{element.getCodeElement()})
                    .build();

        Element savedElement = null;

        try {
            savedElement = elementRepository.save(element);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            throw new InternalErrorException();
        }

        return savedElement;
    }

    @Transactional
    @Override
    public List<Element> saveAll(List<Element> elements) throws ElementNotFoundException, InternalErrorException {
        List<Element> savedElements = new ArrayList<>(elements.size());

        elements.forEach(element -> savedElements.add(save(element)));

        return savedElements;
    }

    @Override
    public Page<Element> findAll(Pageable pageable) {
        return elementRepository.findAll(pageable);
    }

    @Override
    public Element update(Element element) throws ElementNotFoundException, InternalErrorException {

        if (!elementRepository.existsByCodeElement(element.getCodeElement())) {
            throw elementNotFoundException(element.getCodeElement());
        }

        // TODO : check if professeur exists
        // TODO : check if module exists


        Element updatedElement = null;

        try {
            updatedElement = elementRepository.save(element);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            throw new InternalErrorException();
        }

        return updatedElement;
    }

    @Transactional
    @Override
    public List<Element> updateAll(List<Element> elements) throws ElementNotFoundException, InternalErrorException {
        List<Element> updatedElements = new ArrayList<>();
        elements.forEach(element -> updatedElements.add(update(element)));
        return updatedElements;
    }

    @Override
    public void deleteByCodeElement(String codeElement) throws ElementNotFoundException {
        if (!elementRepository.existsByCodeElement(codeElement)) {
            throw elementNotFoundException(codeElement);
        }

        elementRepository.deleteByCodeElement(codeElement);
    }

    @Override
    @Transactional
    public void deleteAllByCodeElement(List<String> codeElements) throws ElementNotFoundException {
        codeElements.forEach(this::deleteByCodeElement);
    }

    @Override
    public Element findByCodeElement(String codeElement) throws ElementNotFoundException {
        return elementRepository.findByCodeElement(codeElement)
                .orElseThrow(() ->
                        elementNotFoundException(codeElement)
                );
    }

    @Override
    public List<Element> findAllByCodeElement(List<String> codeElements) throws ElementNotFoundException {
        List<Element> elements = new ArrayList<>();
        codeElements.forEach(codeElement -> elements.add(this.findByCodeElement(codeElement)));
        return elements;
    }

    @Override
    public List<Element> findByCodeModule(String codeModule) { // TODO : should throw ModuleNotFoundException
        // TODO : check if module exists
        return elementRepository.findByCodeModule(codeModule);
    }

    @Override
    public List<List<Element>> findAllByCodeModule(List<String> codesModule) { // TODO : should throw ModuleNotFoundException
        List<List<Element>> elements = new ArrayList<>();
        codesModule.forEach(codeModule -> elements.add(this.findByCodeModule(codeModule)));
        return elements;
    }

    @Override
    public List<Element> findByCodeProfesseur(String codeProfesseur) { // TODO : should throw ProfesseurNotFoundException
        // TODO : check if professeur exists
        return elementRepository.findByCodeProfesseur(codeProfesseur);
    }

    @Override
    public List<List<Element>> findAllByCodeProfesseur(List<String> codesProfesseur) { // TODO : should throw ProfesseurNotFoundException
        List<List<Element>> elements = new ArrayList<>();
        codesProfesseur.forEach(codeProf -> elements.add(this.findByCodeProfesseur(codeProf)));
        return elements;
    }

    private ElementNotFoundException elementNotFoundException(String codeModule) {
        return ElementNotFoundException.builder()
                .key(CoreConstants.BusinessExceptionMessage.ELEMENT_NOT_FOUND)
                .args(new Object[]{codeModule})
                .build();
    }

}
