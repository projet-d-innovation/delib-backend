package ma.enset.noteservice.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.noteservice.constant.CoreConstants;
import ma.enset.noteservice.dto.ElementByCodeModuleResponse;
import ma.enset.noteservice.dto.ElementResponse;
import ma.enset.noteservice.exception.ElementAlreadyExistsException;
import ma.enset.noteservice.exception.ElementNotFoundException;
import ma.enset.noteservice.exception.ExchangerException;
import ma.enset.noteservice.exception.InternalErrorException;
import ma.enset.noteservice.clients.ElementClient;
import ma.enset.noteservice.model.NoteElement;
import ma.enset.noteservice.repository.NoteElementRepository;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class NoteElementServiceImpl implements NoteElementService {

    private final NoteElementRepository noteElementRepository;


    private final ElementClient elementClient;
    @Override
    public ElementResponse getElement(String codeElement) throws ExchangerException{
        ElementResponse elementResponse = null;
        try {
            elementResponse = elementClient.getElement(codeElement).getBody();
            log.info("elementResponse: {}", elementResponse);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw ExchangerException.builder()
                    .exceptionBody(e.getResponseBodyAsString())
                    .build();
        }
        return elementResponse;
    }
    @Override
    public ElementByCodeModuleResponse getElementByModule(String codeModule) throws ExchangerException{
        ElementByCodeModuleResponse elementResponse = null;
        try {
            elementResponse = elementClient.getElementByModule(codeModule).getBody();
            log.info("elementResponse: {}", elementResponse);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw ExchangerException.builder()
                    .exceptionBody(e.getResponseBodyAsString())
                    .build();
        }
        return elementResponse;
    }

    @Override
    public List<ElementResponse> getElements(List<String> codeElements) throws ExchangerException{
        List<ElementResponse> elementResponses = new ArrayList<>();
        try {
            elementResponses = elementClient.getElements(codeElements).getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw ExchangerException.builder()
                    .exceptionBody(e.getResponseBodyAsString())
                    .build();
        }
        return elementResponses;
    }

//    private SessionResponse getSession(String codeSession) throws ExchangerException{
//        SessionResponse sessionResponse = null;
//        try {
//            sessionResponse = sessionClient.getElement(codeSession).getBody();  TODO: should implelemnt this in the deliberation service
//            log.info("sessionResponse: {}", sessionResponse);
//        } catch (HttpClientErrorException | HttpServerErrorException e) {
//            throw ExchangerException.builder()
//                    .exceptionBody(e.getResponseBodyAsString())
//                    .build();
//        }
//        return sessionResponse;
//    }


    @Override
    public NoteElement save(NoteElement noteElement) throws ElementAlreadyExistsException, InternalErrorException {


        if (noteElementRepository.findById(noteElement.getNoteElementId()).isPresent()) {
            throw ElementAlreadyExistsException.builder()
                    .key(CoreConstants.BusinessExceptionMessage.NOTE_ELEMENT_ALREADY_EXISTS)
                    .args(new Object[]{noteElement.getNoteElementId()})
                    .build();
        }

        getElement(noteElement.getCodeElement());

//      getSession(codeSession);  TODO: implement this in deliberation service


        NoteElement createNoteElement = null;

        try {
            createNoteElement = noteElementRepository.save(noteElement);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            throw new InternalErrorException();
        }
        return createNoteElement;
    }

    @Override
    @Transactional
    public List<NoteElement> saveAll(List<NoteElement> noteElementList) throws ElementAlreadyExistsException, InternalErrorException {
        noteElementList.forEach(element -> {
            if (noteElementRepository.findById(element.getCodeElement()).isPresent()) {
                throw ElementAlreadyExistsException.builder()
                        .key(CoreConstants.BusinessExceptionMessage.NOTE_ELEMENT_ALREADY_EXISTS)
                        .args(new Object[]{element.getCodeElement()})
                        .build();
            }
        });

        getElements(noteElementList.stream().map(NoteElement::getCodeElement).toList());
//      getSession(codeSession);  TODO: implement this in deliberation service


        List<NoteElement> savedNoteElements = new ArrayList<>(noteElementList.size());
        try {
            savedNoteElements = noteElementRepository.saveAll(noteElementList);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            throw new InternalErrorException();
        }
        return savedNoteElements;
    }

    @Override
    public NoteElement findById(String noteElementId) throws ElementNotFoundException {

        return noteElementRepository.findById(noteElementId)
                    .orElseThrow(() -> noteElementNotFoundException(noteElementId));
    }


    @Override
    public NoteElement update(NoteElement noteElement) throws ElementNotFoundException, InternalErrorException {

        NoteElement updatedNoteElement = null;
        try {
            updatedNoteElement = noteElementRepository.save(noteElement);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            throw new InternalErrorException();
        }
        return updatedNoteElement;
    }

    @Override
    public void deleteById(String noteElementId) throws ElementNotFoundException {
        if (!noteElementRepository.findById(noteElementId).isPresent()) {
            throw noteElementNotFoundException(noteElementId);
        }
        noteElementRepository.deleteById(noteElementId);
    }

    @Override
    public List<NoteElement> findByCodeSession(String codeSession) {
//        getSession(codeSession);  TODO: implement this in deliberation service
        return noteElementRepository.findByCodeSession(codeSession);
    }

    @Override
    public List<NoteElement> findAllByNoteElementId(List<String> notElementIdList) throws ElementNotFoundException {
        List<NoteElement> noteElementList = new ArrayList<>();
        notElementIdList.forEach(noteElementId -> noteElementList.add(this.findById(noteElementId)));
        return noteElementList;
    }

    @Transactional
    @Override
    public List<NoteElement> updateAll(List<NoteElement> noteElementList) throws ElementNotFoundException, InternalErrorException {

        noteElementList.forEach(noteElement -> {
            if (!noteElementRepository.findById(noteElement.getNoteElementId()).isPresent()) {
                throw noteElementNotFoundException(noteElement.getNoteElementId());
            }
        });
        List<NoteElement> updatedNoteElements = new ArrayList<>();
        try {
            updatedNoteElements = noteElementRepository.saveAll(noteElementList);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            throw new InternalErrorException();
        }
        return updatedNoteElements;
    }

    @Override
    public NoteElement findByCodeSessionAndCodeElement(String codeSession, String codeElement) {
        return noteElementRepository.findByCodeSessionAndCodeElement(codeSession, codeElement);
    }

    //
    private ElementNotFoundException noteElementNotFoundException(String noteElementId) {
        return ElementNotFoundException.builder()
                .key(CoreConstants.BusinessExceptionMessage.NOTE_ELEMENT_NOT_FOUND)
                .args(new Object[]{noteElementId})
                .build();
    }

}
