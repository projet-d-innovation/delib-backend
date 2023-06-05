package ma.enset.noteservice.service;

import ma.enset.noteservice.dto.noteelement.GroupedNotesElementResponse;
import ma.enset.noteservice.dto.noteelement.NoteElementCreationRequest;
import ma.enset.noteservice.dto.noteelement.NoteElementResponse;
import ma.enset.noteservice.dto.noteelement.NoteElementUpdateRequest;
import ma.enset.noteservice.exception.ElementAlreadyExistsException;
import ma.enset.noteservice.exception.ElementNotFoundException;
import ma.enset.noteservice.model.NoteElement;

import java.util.List;
import java.util.Set;

public interface NoteElementService {
    NoteElementResponse save(NoteElementCreationRequest noteElement) throws ElementAlreadyExistsException;

    List<NoteElementResponse> saveAll(List<NoteElementCreationRequest> noteElements) throws ElementAlreadyExistsException;

    NoteElementResponse update(NoteElementUpdateRequest noteElement) throws ElementNotFoundException;

    List<NoteElementResponse> updateAll(Set<NoteElementUpdateRequest> noteElements) throws ElementNotFoundException;

    GroupedNotesElementResponse getNotesBySession(String sessionId, boolean includeElement) throws ElementNotFoundException;

    Set<GroupedNotesElementResponse> getNotesBySessions(Set<String> sessionIdList, boolean includeElement) throws ElementNotFoundException;

    void deleteBySessionAndElement(String sessionId, String codeElement) throws ElementNotFoundException;

    void deleteAllBySessionAndElement(String sessionId, Set<String> codeElements) throws ElementNotFoundException;

    void deleteBySession(String sessionId) throws ElementNotFoundException;

    void deleteAllBySession(Set<String> sessionIdList) throws ElementNotFoundException;
}
