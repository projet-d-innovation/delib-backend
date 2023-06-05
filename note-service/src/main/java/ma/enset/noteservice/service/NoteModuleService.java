package ma.enset.noteservice.service;

import ma.enset.noteservice.dto.notemodule.GroupedNotesModuleResponse;
import ma.enset.noteservice.dto.notemodule.NoteModuleCreationRequest;
import ma.enset.noteservice.dto.notemodule.NoteModuleResponse;
import ma.enset.noteservice.exception.ElementAlreadyExistsException;
import ma.enset.noteservice.exception.ElementNotFoundException;
import ma.enset.noteservice.model.NoteElement;
import ma.enset.noteservice.model.NoteModule;

import java.util.List;
import java.util.Set;

public interface NoteModuleService {
    NoteModuleResponse saveOrUpdate(NoteModuleCreationRequest noteModule) throws ElementAlreadyExistsException;

    List<NoteModuleResponse> saveOrUpdateAll(List<NoteModuleCreationRequest> noteModules) throws ElementAlreadyExistsException;

    GroupedNotesModuleResponse getNotesBySession(String sessionId) throws ElementNotFoundException;

    Set<GroupedNotesModuleResponse> getNotesBySessions(Set<String> sessionIdList) throws ElementNotFoundException;

    void deleteBySessionAndElement(String sessionId, String codeElement) throws ElementNotFoundException;

    void deleteAllBySessionAndElement(String sessionId, Set<String> codeElements) throws ElementNotFoundException;

    void deleteBySession(String sessionId) throws ElementNotFoundException;

    void deleteAllBySession(Set<String> sessionIdList) throws ElementNotFoundException;
}
