package ma.enset.sessionuniversitaireservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.sessionuniversitaireservice.constant.CoreConstants;
import ma.enset.sessionuniversitaireservice.dto.SessionCreationRequest;
import ma.enset.sessionuniversitaireservice.dto.SessionPagingResponse;
import ma.enset.sessionuniversitaireservice.dto.SessionResponse;
import ma.enset.sessionuniversitaireservice.dto.SessionUpdateRequest;
import ma.enset.sessionuniversitaireservice.exception.DuplicateEntryException;
import ma.enset.sessionuniversitaireservice.exception.ElementAlreadyExistsException;
import ma.enset.sessionuniversitaireservice.exception.ElementNotFoundException;
import ma.enset.sessionuniversitaireservice.mapper.SessionUniversitaireMapper;
import ma.enset.sessionuniversitaireservice.model.SessionUniversitaire;
import ma.enset.sessionuniversitaireservice.repository.SessionUniversitaireRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class SessionUniversitaireServiceImpl implements SessionUniversitaireService {

    private final String ELEMENT_TYPE = "Session Universitaire";
    private final String ID_FIELD_NAME = "ID";
    private final SessionUniversitaireRepository repository;
    private final SessionUniversitaireMapper mapper;

    @Override
    public SessionResponse save(SessionCreationRequest request) throws ElementAlreadyExistsException {

        SessionUniversitaire session = mapper.toSessionUniversitaire(request);

        try {
            return mapper.toSessionResponse(repository.save(session));
        } catch (DataIntegrityViolationException e) {
            throw new ElementAlreadyExistsException(
                CoreConstants.BusinessExceptionMessage.ALREADY_EXISTS,
                new Object[] {ELEMENT_TYPE, ID_FIELD_NAME, request.id()},
                null
            );
        }
    }

    @Override
    public List<SessionResponse> saveAll(List<SessionCreationRequest> request) throws ElementAlreadyExistsException,
                                                                                        DuplicateEntryException {

        int uniqueSessionsCount = (int) request.stream()
                                                .map(SessionCreationRequest::id)
                                                .distinct().count();

        if (uniqueSessionsCount != request.size()) {
            throw new DuplicateEntryException(
                CoreConstants.BusinessExceptionMessage.DUPLICATE_ENTRY,
                new Object[]{ELEMENT_TYPE}
            );
        }

        List<SessionUniversitaire> foundSessions = repository.findAllById(
            request.stream()
                    .map(SessionCreationRequest::id)
                    .collect(Collectors.toSet())
        );

        if (!foundSessions.isEmpty()) {
            throw new ElementAlreadyExistsException(
                CoreConstants.BusinessExceptionMessage.MANY_ALREADY_EXISTS,
                new Object[]{ELEMENT_TYPE},
                foundSessions.stream()
                            .map(SessionUniversitaire::getId)
                            .toList()
            );
        }

        List<SessionUniversitaire> sessions = mapper.toSessionUniversitaireList(request);

        return mapper.toSessionResponseList(repository.saveAll(sessions));
    }

    @Override
    public boolean existAllByIds(Set<String> ids) throws ElementNotFoundException {

        List<String> foundSessionsIds = repository.findAllById(ids).stream()
                                                    .map(SessionUniversitaire::getId).toList();

        if (ids.size() != foundSessionsIds.size()) {
            throw new ElementNotFoundException(
                CoreConstants.BusinessExceptionMessage.MANY_NOT_FOUND,
                new Object[] {ELEMENT_TYPE},
                ids.stream()
                    .filter(id -> !foundSessionsIds.contains(id))
                    .toList()
            );
        }

        return true;
    }

    @Override
    public SessionResponse findById(String id) throws ElementNotFoundException {

        return mapper.toSessionResponse(
            repository.findById(id).orElseThrow(() ->
                new ElementNotFoundException(
                    CoreConstants.BusinessExceptionMessage.NOT_FOUND,
                    new Object[] {ELEMENT_TYPE, ID_FIELD_NAME, id},
                    null
                )
            )
        );
    }

    @Override
    public List<SessionResponse> findAllByIds(Set<String> ids) throws ElementNotFoundException {

        List<SessionResponse> response = mapper.toSessionResponseList(repository.findAllById(ids));

        if (ids.size() != response.size()) {
            throw new ElementNotFoundException(
                CoreConstants.BusinessExceptionMessage.MANY_NOT_FOUND,
                new Object[] {ELEMENT_TYPE},
                ids.stream()
                    .filter(
                        id -> !response.stream()
                            .map(SessionResponse::id)
                            .toList()
                            .contains(id)
                    )
                    .toList()
            );
        }

        return response;
    }

    @Override
    public SessionPagingResponse findAll(int page, int size) {
        return mapper.toPagingResponse(repository.findAll(PageRequest.of(page, size)));
    }

    @Override
    public SessionResponse update(String id, SessionUpdateRequest request) throws ElementNotFoundException {

        SessionUniversitaire session = repository.findById(id).orElseThrow(() ->
            new ElementNotFoundException(
                CoreConstants.BusinessExceptionMessage.NOT_FOUND,
                new Object[] {ELEMENT_TYPE, ID_FIELD_NAME, id},
                null
            )
        );

        mapper.updateSessionUniversitaireFromDTO(request, session);

        return mapper.toSessionResponse(repository.save(session));
    }

    @Override
    public void deleteById(String id) throws ElementNotFoundException {

        if (!repository.existsById(id)) {
            throw new ElementNotFoundException(
                CoreConstants.BusinessExceptionMessage.NOT_FOUND,
                new Object[] {ELEMENT_TYPE, ID_FIELD_NAME, id},
                null
            );
        }

        repository.deleteById(id);
    }

    @Override
    public void deleteAllByIds(Set<String> ids) throws ElementNotFoundException {
        existAllByIds(ids);

        repository.deleteAllById(ids);
    }
}
