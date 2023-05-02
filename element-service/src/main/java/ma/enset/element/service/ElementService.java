package ma.enset.element.service;

import ma.enset.element.exception.InternalErrorException;
import ma.enset.element.model.Element;
import ma.enset.element.exception.ElementAlreadyExistsException;
import ma.enset.element.exception.ElementNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ElementService {
    Element save(Element element) throws ElementAlreadyExistsException, InternalErrorException;

    List<Element> saveAll(List<Element> elements) throws ElementAlreadyExistsException, InternalErrorException;

    Element findByCodeElement(String codeElement) throws ElementNotFoundException;

    List<Element> findAllByCodeElement(List<String> codesElement) throws ElementNotFoundException;

    Page<Element> findAll(Pageable pageable);

    Element update(Element element) throws ElementNotFoundException, InternalErrorException;;
    List<Element> updateAll(List<Element> elements) throws ElementNotFoundException, InternalErrorException;;

    void deleteByCodeElement(String codeElement) throws ElementNotFoundException;

    void deleteAllByCodeElement(List<String> codesElement) throws ElementNotFoundException;

    List<Element> findByCodeModule(String codeModule);

    List<List<Element>> findAllByCodeModule(List<String> codesModule);

    List<Element> findByCodeProfesseur(String codeProfesseur);

    List<List<Element>> findAllByCodeProfesseur(List<String> codesProfesseur);
}
