package ma.enset.noteservice.service;

import ma.enset.noteservice.dto.ElementByCodeModuleResponse;
import ma.enset.noteservice.dto.ElementResponse;
import ma.enset.noteservice.exception.ElementAlreadyExistsException;
import ma.enset.noteservice.exception.ElementNotFoundException;
import ma.enset.noteservice.exception.ExchangerException;
import ma.enset.noteservice.exception.InternalErrorException;
import ma.enset.noteservice.model.NoteElement;
import ma.enset.noteservice.model.NoteModule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.swing.text.Element;
import java.util.List;

public interface NoteElementService {
    ElementResponse getElement(String codeElement) throws ExchangerException;

    ElementByCodeModuleResponse getElementByModule(String codeModule) throws ExchangerException;

    List<ElementResponse> getElements(List<String> codeElements) throws ExchangerException;

    NoteElement save(NoteElement note) throws ElementAlreadyExistsException, InternalErrorException;
    List<NoteElement> saveAll(List<NoteElement> modules) throws ElementAlreadyExistsException, InternalErrorException;
    NoteElement findById(String noteElementId) throws ElementNotFoundException;
    NoteElement update(NoteElement noteElement) throws ElementNotFoundException, InternalErrorException;
    void deleteById(String codeModule) throws ElementNotFoundException;

    List<NoteElement> findByCodeSession(String codeSession);

    List<NoteElement> findAllByNoteElementId(List<String> noteElementList);

    List<NoteElement> updateAll(List<NoteElement> noteElementList);
//    List<NoteElement> findAllByCodeElement(String codeElement);

    NoteElement findByCodeSessionAndCodeElement(String codeSession, String codeElement);

}
