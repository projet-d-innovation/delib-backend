package ma.enset.noteservice.service;

import ma.enset.noteservice.exception.ElementAlreadyExistsException;
import ma.enset.noteservice.exception.ElementNotFoundException;

import ma.enset.noteservice.exception.InternalErrorException;
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

    Page<NoteModule> findAllByCodeSession(String codeSession, Pageable pageRequest);
}
