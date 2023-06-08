package ma.enset.deliberationservice.service;

import ma.enset.deliberationservice.dto.RequiredSearchParams;
import ma.enset.deliberationservice.dto.session.SessionCreationRequest;
import ma.enset.deliberationservice.dto.session.SessionPagingResponse;
import ma.enset.deliberationservice.dto.session.SessionResponse;
import ma.enset.deliberationservice.dto.session.SessionUpdateRequest;
import ma.enset.deliberationservice.exception.ElementNotFoundException;
import ma.enset.deliberationservice.model.SessionType;

import java.util.List;
import java.util.Set;

public interface SessionService {
    SessionResponse save(SessionCreationRequest session);

    List<SessionResponse> saveAll(List<SessionCreationRequest> sessions);

    SessionResponse findById(String id) throws ElementNotFoundException;

    List<SessionResponse> findAllById(Set<String> ids) throws ElementNotFoundException;

    // TODO (ahmed) : findAll should be removed | it's just for debugging
    SessionPagingResponse findAll(int page, int size);

    List<SessionResponse> search(String codeFiliere,
                                 String codeSemestre,
                                 String codeSessionUniversitaire,
                                 Integer annee,
                                 SessionType type);

    SessionResponse update(String id, SessionUpdateRequest session) throws ElementNotFoundException;

    void deleteById(String id) throws ElementNotFoundException;

    void deleteById(Set<String> ids) throws ElementNotFoundException;

    boolean existsAllId(Set<String> codesSessions) throws ElementNotFoundException;

}
