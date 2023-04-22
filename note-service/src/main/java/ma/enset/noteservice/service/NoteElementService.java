package ma.enset.noteservice.service;

import ma.enset.noteservice.exception.ElementAlreadyExistsException;
import ma.enset.noteservice.exception.ElementNotFoundException;
import ma.enset.noteservice.exception.InternalErrorException;
import ma.enset.noteservice.model.NoteElement;
import ma.enset.noteservice.model.NoteModule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NoteElementService {
    NoteElement save(NoteElement note) throws ElementAlreadyExistsException, InternalErrorException;
    List<NoteElement> saveAll(List<NoteElement> modules) throws ElementAlreadyExistsException, InternalErrorException;
    NoteElement findById(String noteElementId) throws ElementNotFoundException;
    NoteElement update(NoteElement noteElement) throws ElementNotFoundException, InternalErrorException;
    void deleteById(String codeModule) throws ElementNotFoundException;

    Page<NoteElement> findByCodeSession(String codeSession, Pageable pageRequest);
}
