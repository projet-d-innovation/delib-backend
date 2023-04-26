package ma.enset.noteservice.service;

import ma.enset.noteservice.dto.ElementResponse;
import ma.enset.noteservice.exception.ElementAlreadyExistsException;
import ma.enset.noteservice.exception.ElementNotFoundException;

import ma.enset.noteservice.exception.InternalErrorException;
import ma.enset.noteservice.model.NoteElement;
import ma.enset.noteservice.model.NoteModule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NoteModuleService {
    NoteModule save(NoteModule noteModule) throws ElementAlreadyExistsException, InternalErrorException;
    List<NoteModule> saveAll(List<NoteModule> noteModuleList) throws ElementAlreadyExistsException, InternalErrorException;
    NoteModule findById(String noteModuleId) throws ElementNotFoundException;
    NoteModule update(NoteModule noteModule) throws ElementNotFoundException, InternalErrorException;
    void deleteById(String codeModule) throws ElementNotFoundException;

    List<NoteModule> findAllByCodeSession(String codeSession);

    List<NoteModule> findAllByNoteModuleId(List<String> noteModuleIdList);


    List<NoteModule> updateAll(List<NoteModule> noteElementList);

    NoteModule findByCodeModuleAndCodeSession(String codeModule, String codeSession);

    NoteModule calculateNoteModule(NoteElement noteElement, String codeModule, List<ElementResponse> elementResponses, NoteElement noteElement1, NoteElement noteElement2);
    }
