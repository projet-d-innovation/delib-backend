package ma.enset.element.service;

import lombok.AllArgsConstructor;
import ma.enset.element.constant.CoreConstants;
import ma.enset.element.model.Element;
import ma.enset.element.exception.ElementAlreadyExistsException;
import ma.enset.element.exception.ElementNotFoundException;
import ma.enset.element.repository.ElementRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ElementServiceImpl implements ElementService {
    private final ElementRepository elementRepository;

    @Override
    public Element create(Element element) throws ElementAlreadyExistsException {
        Element createdElement = null;

        if (elementRepository.existsById(element.getCodeElement()))
            throw ElementAlreadyExistsException.builder()
                    .key(CoreConstants.BusinessExceptionMessage.ELEMENT_ALREADY_EXISTS)
                    .args(new Object[]{"code element", element.getCodeElement()})
                    .build();

        try {
            createdElement = elementRepository.save(element);
        } catch (Exception e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                throw ElementAlreadyExistsException.builder()
                        .key(CoreConstants.BusinessExceptionMessage.INTERNAL_ERROR)
                        .args(new Object[]{"code element", element.getCodeElement()})
                        .build();
            }
        }

        return createdElement;
    }

    @Override
    public List<Element> createMany(List<Element> elements) throws ElementAlreadyExistsException {
        List<Element> createdElements = new ArrayList<>();

        for (Element element : elements) {
            if (elementRepository.existsById(element.getCodeElement()))
                throw ElementAlreadyExistsException.builder()
                        .key(CoreConstants.BusinessExceptionMessage.ELEMENT_ALREADY_EXISTS)
                        .args(new Object[]{"code element", element.getCodeElement()})
                        .build();

            try {
                createdElements.add(elementRepository.save(element));
            } catch (Exception e) {
                if (e.getCause() instanceof ConstraintViolationException) {
                    throw ElementAlreadyExistsException.builder()
                            .key(CoreConstants.BusinessExceptionMessage.INTERNAL_ERROR)
                            .args(new Object[]{"code element", element.getCodeElement()})
                            .build();
                }
            }
        }

        return createdElements;
    }

    @Override
    public Page<Element> findAll(Pageable pageable) {
        return elementRepository.findAll(pageable);
    }

    @Override
    public Element update(Element element) throws ElementNotFoundException {

        if (!elementRepository.existsById(element.getCodeElement())) {
            throw ElementNotFoundException.builder()
                    .key(CoreConstants.BusinessExceptionMessage.ELEMENT_NOT_FOUND)
                    .args(new Object[]{"code element", element.getCodeElement()})
                    .build();
        }

        // TODO : check if professeur exists
        // TODO : check if module exists


        Element updatedElement = null;

        try {
            updatedElement = elementRepository.save(element);
        } catch (Exception e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                throw ElementAlreadyExistsException.builder()
                        .key(CoreConstants.BusinessExceptionMessage.ELEMENT_ALREADY_EXISTS)
                        .args(new Object[]{"code element", element.getCodeElement()})
                        .build();
            }
        }

        return updatedElement;
    }

    @Override
    public void deleteById(String codeElement) throws ElementNotFoundException {
        if (!elementRepository.existsById(codeElement)) {
            throw ElementNotFoundException.builder()
                    .key(CoreConstants.BusinessExceptionMessage.ELEMENT_NOT_FOUND)
                    .args(new Object[]{"code element", codeElement})
                    .build();
        }

        elementRepository.deleteById(codeElement);
    }

    @Override
    public void deleteManyById(List<String> codeElements) throws ElementNotFoundException {
        for (String codeElement : codeElements) {
            this.deleteById(codeElement);
        }
    }

    @Override
    public Element findById(String codeElement) throws ElementNotFoundException {
        return elementRepository.findById(codeElement)
                .orElseThrow(() ->
                        ElementNotFoundException.builder()
                                .key(CoreConstants.BusinessExceptionMessage.ELEMENT_NOT_FOUND)
                                .args(new Object[]{"code element", codeElement})
                                .build()
                );
    }

    @Override
    public List<Element> findManyById(List<String> codeElements) throws ElementNotFoundException {
        List<Element> elements = new ArrayList<>();
        for (String codeElement : codeElements) {
            elements.add(findById(codeElement));
        }
        return elements;
    }

    @Override
    public List<Element> findByModule(String codeModule) {
        // TODO : check if module exists
        return elementRepository.findByCodeModule(codeModule);
    }

    @Override
    public List<List<Element>> findManyByModule(List<String> codesModule) {
        List<List<Element>> elements = new ArrayList<>();
        codesModule.forEach(codeModule -> elements.add(this.findByModule(codeModule)));
        return elements;
    }

    @Override
    public List<Element> findByProfesseur(String codeProfesseur) {
        // TODO : check if professeur exists
        return elementRepository.findByCodeProfesseur(codeProfesseur);
    }

    @Override
    public List<List<Element>> findManyByProfesseur(List<String> codesProfesseur) {
        List<List<Element>> elements = new ArrayList<>();
        codesProfesseur.forEach(codeProf -> elements.add(this.findByProfesseur(codeProf)));
        return elements;
    }


}
