package ma.enset.noteservice.service;

import ma.enset.noteservice.dto.notemodule.GroupedNotesModuleResponse;
import ma.enset.noteservice.dto.notemodule.NoteModuleRequest;
import ma.enset.noteservice.dto.notemodule.NoteModuleResponse;
import ma.enset.noteservice.exception.ElementAlreadyExistsException;
import ma.enset.noteservice.exception.ElementNotFoundException;

import java.util.List;
import java.util.Set;

public interface NoteModuleService {
    NoteModuleResponse saveOrUpdate(NoteModuleRequest noteModule) throws ElementAlreadyExistsException;

    List<NoteModuleResponse> saveOrUpdateAll(List<NoteModuleRequest> noteModules) throws ElementAlreadyExistsException;

    GroupedNotesModuleResponse getNotesBySession(String sessionId, boolean includeModule, boolean includeNoteElement) throws ElementNotFoundException;

    Set<GroupedNotesModuleResponse> getNotesBySessions(Set<String> sessionIdList, boolean includeModule, boolean includeNoteElement) throws ElementNotFoundException;

    void deleteBySessionAndModule(String sessionId, String codeModule) throws ElementNotFoundException;

    void deleteAllBySessionAndModule(String sessionId, Set<String> codeModules) throws ElementNotFoundException;

    void deleteBySession(String sessionId) throws ElementNotFoundException;

    void deleteAllBySession(Set<String> sessionIdList) throws ElementNotFoundException;
}
