package ma.enset.element.service;

import ma.enset.element.model.Element;
import ma.enset.element.exception.ElementAlreadyExistsException;
import ma.enset.element.exception.ElementNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ElementService {
    Element create(Element element) throws ElementAlreadyExistsException;

    List<Element> createMany(List<Element> elements) throws ElementAlreadyExistsException;

    Element findById(String codeElement) throws ElementNotFoundException;

    List<Element> findManyById(List<String> codesElement) throws ElementNotFoundException;

    Page<Element> findAll(Pageable pageable);

    Element update(Element element) throws ElementNotFoundException;

    void deleteById(String codeElement) throws ElementNotFoundException;

    void deleteManyById(List<String> codesElement) throws ElementNotFoundException;

    List<Element> findByModule(String codeModule);// TODO: should throw ModuleNotFoundException

    List<List<Element>> findManyByModule(List<String> codesModule);// TODO: should throw ModuleNotFoundException

    List<Element> findByProfesseur(String codeProfesseur);// TODO: should throw ProfesseurNotFoundException

    List<List<Element>> findManyByProfesseur(List<String> codesProfesseur);// TODO: should throw ProfesseurNotFoundException
}
