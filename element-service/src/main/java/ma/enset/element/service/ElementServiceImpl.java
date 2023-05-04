package ma.enset.element.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.element.client.ModuleClient;
import ma.enset.element.client.UtilisateurClient;
import ma.enset.element.constant.CoreConstants;
import ma.enset.element.dto.ModuleResponse;
import ma.enset.element.dto.ProfesseurResponse;
import ma.enset.element.exception.ElementAlreadyExistsException;
import ma.enset.element.exception.ElementNotFoundException;
import ma.enset.element.exception.ExchangerException;
import ma.enset.element.exception.InternalErrorException;
import ma.enset.element.model.Element;
import ma.enset.element.repository.ElementRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class ElementServiceImpl implements ElementService {
    private final ElementRepository elementRepository;
    private final ModuleClient moduleClient;
    private final UtilisateurClient utilisateurClient;
    @Override
    public Element save(Element element) throws ElementAlreadyExistsException, InternalErrorException {

        if (elementRepository.existsByCodeElement(element.getCodeElement()))
            throw ElementAlreadyExistsException.builder()
                    .key(CoreConstants.BusinessExceptionMessage.ELEMENT_ALREADY_EXISTS)
                    .args(new Object[]{element.getCodeElement()})
                    .build();


        getProfesseur(element.getCodeProfesseur());

        getModule(element.getCodeModule());

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


        elements.forEach(element -> {
            if (elementRepository.existsByCodeElement(element.getCodeElement())) {
                throw ElementAlreadyExistsException.builder()
                        .key(CoreConstants.BusinessExceptionMessage.ELEMENT_ALREADY_EXISTS)
                        .args(new Object[]{element.getCodeElement()})
                        .build();
            }
        });

        List<String> codesProfesseur = elements.stream().map(Element::getCodeProfesseur).toList();
        getProfesseurs(codesProfesseur);

        List<String> codesModule = elements.stream().map(Element::getCodeModule).toList();
        getModules(codesModule);

        List<Element> savedElements;
        try {
            savedElements = elementRepository.saveAll(elements);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            throw new InternalErrorException();
        }
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

        getProfesseur(element.getCodeProfesseur());

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

        elements.forEach(element -> {
            if (!elementRepository.existsByCodeElement(element.getCodeElement())) {
                throw elementNotFoundException(element.getCodeElement());
            }
        });

        List<String> codeProfesseurs = elements.stream().map(Element::getCodeProfesseur).toList();
        getProfesseurs(codeProfesseurs);

        List<Element> updatedElements;
        try {
            updatedElements = elementRepository.saveAll(elements);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            throw new InternalErrorException();
        }
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
                .orElseThrow(() -> elementNotFoundException(codeElement));
    }

    @Override
    public List<Element> findAllByCodeElement(List<String> codeElements) throws ElementNotFoundException {
        List<Element> elements = new ArrayList<>();
        codeElements.forEach(codeElement -> elements.add(elementRepository.findById(codeElement).orElseThrow(() -> elementNotFoundException(codeElement))));
        return elements;
    }

    @Override
    public List<Element> findByCodeModule(String codeModule) {
        return elementRepository.findByCodeModule(codeModule);
    }

    @Override
    public List<List<Element>> findAllByCodeModule(List<String> codesModule) {
        List<List<Element>> elements = new ArrayList<>();
        codesModule.forEach(codeModule -> elements.add(this.findByCodeModule(codeModule)));
        return elements;
    }

    @Override
    public List<Element> findByCodeProfesseur(String codeProfesseur) throws ExchangerException {
        return elementRepository.findByCodeProfesseur(codeProfesseur);
    }

    @Override
    public List<List<Element>> findAllByCodeProfesseur(List<String> codesProfesseur) {
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

    private ProfesseurResponse getProfesseur(String codeProfesseur) throws ExchangerException{
        ProfesseurResponse professeurResponse;
        try{
            professeurResponse = utilisateurClient.getProfesseur(codeProfesseur).getBody();
        }catch (HttpClientErrorException | HttpServerErrorException e) {
            throw ExchangerException.builder()
                    .exceptionBody(e.getResponseBodyAsString())
                    .build();
        }
        return professeurResponse;
    }

    private List<ProfesseurResponse> getProfesseurs(List<String> codesProfesseur) throws ExchangerException{
        List<ProfesseurResponse> professeurResponses = new ArrayList<>();
            try {
                professeurResponses = utilisateurClient.findByCodes(codesProfesseur).getBody();
            } catch (HttpClientErrorException | HttpServerErrorException e) {
                throw ExchangerException.builder()
                        .exceptionBody(e.getResponseBodyAsString())
                        .build();
            }
        return professeurResponses;
    }

    private ModuleResponse getModule(String codeModule) throws ExchangerException {
        ModuleResponse moduleResponse;
        try{
            moduleResponse = moduleClient.getModule(codeModule).getBody();
        }catch (HttpClientErrorException | HttpServerErrorException e) {
            throw ExchangerException.builder()
                    .exceptionBody(e.getResponseBodyAsString())
                    .build();
        }
        return moduleResponse;
    }

    private List<ModuleResponse> getModules(List<String> codesModule) throws ExchangerException{
        List<ModuleResponse> moduleResponses = new ArrayList<>();
        try {
            codesModule.forEach(codeModule -> moduleResponses.add(moduleClient.getModule(codeModule).getBody()));
            // TODO: implement that in module service
            // moduleResponses = moduleClient.findByCodes(codesModule).getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw ExchangerException.builder()
                    .exceptionBody(e.getResponseBodyAsString())
                    .build();
        }
        return moduleResponses;
    }

}
