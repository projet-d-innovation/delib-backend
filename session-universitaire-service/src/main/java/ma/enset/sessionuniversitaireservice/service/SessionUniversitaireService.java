package ma.enset.sessionuniversitaireservice.service;


import ma.enset.sessionuniversitaireservice.dto.SessionCreationRequest;
import ma.enset.sessionuniversitaireservice.dto.SessionPagingResponse;
import ma.enset.sessionuniversitaireservice.dto.SessionResponse;
import ma.enset.sessionuniversitaireservice.dto.SessionUpdateRequest;
import ma.enset.sessionuniversitaireservice.exception.DuplicateEntryException;
import ma.enset.sessionuniversitaireservice.exception.ElementAlreadyExistsException;
import ma.enset.sessionuniversitaireservice.exception.ElementNotFoundException;

import java.util.List;
import java.util.Set;

public interface SessionUniversitaireService {

    SessionResponse save(SessionCreationRequest request) throws ElementAlreadyExistsException;

    List<SessionResponse> saveAll(List<SessionCreationRequest> request) throws ElementAlreadyExistsException, DuplicateEntryException;

    boolean existAllByIds(Set<String> ids) throws ElementNotFoundException;

    SessionResponse findById(String id) throws ElementNotFoundException;

    List<SessionResponse> findAllByIds(Set<String> ids) throws ElementNotFoundException;

    SessionPagingResponse findAll(int page, int size);

    SessionResponse update(String id, SessionUpdateRequest request) throws ElementNotFoundException;

    void deleteById(String id) throws ElementNotFoundException;

    void deleteAllByIds(Set<String> ids) throws ElementNotFoundException;

}
