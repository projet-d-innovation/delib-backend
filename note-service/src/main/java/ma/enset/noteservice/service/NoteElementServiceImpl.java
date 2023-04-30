package ma.enset.noteservice.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.noteservice.constant.CoreConstants;
import ma.enset.noteservice.exception.ElementAlreadyExistsException;
import ma.enset.noteservice.exception.ElementNotFoundException;
import ma.enset.noteservice.exception.InternalErrorException;
import ma.enset.noteservice.feign.ElementServiceFeignClient;
import ma.enset.noteservice.model.NoteElement;
import ma.enset.noteservice.repository.NoteElementRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class NoteElementServiceImpl implements NoteElementService {

    private final NoteElementRepository noteElementRepository;

    private final ElementServiceFeignClient elementServiceFeignClient;

    @Override
    public NoteElement save(NoteElement noteElement) throws ElementAlreadyExistsException, InternalErrorException {

        if (elementServiceFeignClient.getElementByCode(noteElement.getCodeElement()).getStatusCode() != HttpStatus.OK)
            return null;

        if (noteElementRepository.findById(noteElement.getNoteElementId()).isPresent()) {
            throw ElementAlreadyExistsException.builder()
                    .key(CoreConstants.BusinessExceptionMessage.NOTE_ELEMENT_ALREADY_EXISTS)
                    .args(new Object[]{noteElement.getNoteElementId()})
                    .build();
        }

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
        List<NoteElement> createdNoteElement = new ArrayList<>(noteElementList.size());
        noteElementList.forEach(noteElement -> {
            if (elementServiceFeignClient.getElementByCode(noteElement.getCodeElement()).getStatusCode() != HttpStatus.OK)
                return ;
            createdNoteElement.add(save(noteElement));
        });
        return createdNoteElement;
    }

    @Override
    public NoteElement findById(String noteElementId) throws ElementNotFoundException {

        return noteElementRepository.findById(noteElementId)
                    .orElseThrow(() -> noteElementNotFoundException(noteElementId));
    }



    @Override
    public NoteElement update(NoteElement noteElement) throws ElementNotFoundException, InternalErrorException {

        if (elementServiceFeignClient.getElementByCode(noteElement.getCodeElement()).getStatusCode() != HttpStatus.OK)
            return null;

        if (!noteElementRepository.findById(noteElement.getNoteElementId()).isPresent()) {
            throw noteElementNotFoundException(noteElement.getNoteElementId());
        }

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
        List<NoteElement> updatedNoteElements = new ArrayList<>();
        noteElementList.forEach(noteElement -> updatedNoteElements.add(update(noteElement)));
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
