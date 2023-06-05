package ma.enset.deliberationservice.service;

import ma.enset.deliberationservice.dto.session.SessionCreationRequest;
import ma.enset.deliberationservice.dto.session.SessionPagingResponse;
import ma.enset.deliberationservice.dto.session.SessionResponse;
import ma.enset.deliberationservice.dto.session.SessionUpdateRequest;
import ma.enset.deliberationservice.exception.ElementNotFoundException;

import java.util.List;
import java.util.Set;

public interface SessionService {
    SessionResponse save(SessionCreationRequest session);

    List<SessionResponse> saveAll(List<SessionCreationRequest> sessions);

    SessionResponse findById(String id) throws ElementNotFoundException;

    List<SessionResponse> findAllById(Set<String> ids) throws ElementNotFoundException;

    SessionPagingResponse findAll(int page, int size);

    SessionResponse update(String id, SessionUpdateRequest session) throws ElementNotFoundException;

    void deleteById(String id) throws ElementNotFoundException;

    void deleteById(Set<String> ids) throws ElementNotFoundException;

    boolean existsAllId(Set<String> codesSessions) throws ElementNotFoundException;

}
