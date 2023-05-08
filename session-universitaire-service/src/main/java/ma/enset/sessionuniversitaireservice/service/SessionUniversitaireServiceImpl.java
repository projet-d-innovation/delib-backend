package ma.enset.sessionuniversitaireservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.sessionuniversitaireservice.constant.CoreConstants;
import ma.enset.sessionuniversitaireservice.dto.SessionCreationRequest;
import ma.enset.sessionuniversitaireservice.dto.SessionPagingResponse;
import ma.enset.sessionuniversitaireservice.dto.SessionResponse;
import ma.enset.sessionuniversitaireservice.dto.SessionUpdateRequest;
import ma.enset.sessionuniversitaireservice.exception.ElementAlreadyExistsException;
import ma.enset.sessionuniversitaireservice.exception.ElementNotFoundException;
import ma.enset.sessionuniversitaireservice.mapper.SessionUniversitaireMapper;
import ma.enset.sessionuniversitaireservice.model.SessionUniversitaire;
import ma.enset.sessionuniversitaireservice.repository.SessionUniversitaireRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public SessionResponse save(final SessionCreationRequest request)
        throws ElementAlreadyExistsException {

        if (repository.existsById(request.id())) {
            throw new ElementAlreadyExistsException(
                CoreConstants.BusinessExceptionMessage.ALREADY_EXISTS,
                new Object[] {ELEMENT_TYPE, ID_FIELD_NAME, request.id()},
                null
            );
        }

        final SessionUniversitaire session = mapper.toSessionUniversitaire(request);

        return mapper.toSessionResponse(repository.save(session));
    }

    @Override
    public List<SessionResponse> saveAll(final List<SessionCreationRequest> request)
        throws ElementAlreadyExistsException{

        final List<SessionUniversitaire> foundSessions = repository.findAllById(
            request.stream()
                .map(SessionCreationRequest::id)
                .collect(Collectors.toSet())
        );

        if (!foundSessions.isEmpty()) {
            throw new ElementAlreadyExistsException(
                CoreConstants.BusinessExceptionMessage.MANY_ALREADY_EXISTS,
                new Object[] {ELEMENT_TYPE},
                foundSessions.stream()
                    .map(SessionUniversitaire::getId)
                    .toList()
            );
        }

        final List<SessionUniversitaire> sessionsUniversitaire =
            mapper.toSessionUniversitaireList(request);

        return mapper.toSessionResponseList(repository.saveAll(sessionsUniversitaire));
    }

    @Override
    public SessionResponse findById(final String id) throws ElementNotFoundException {
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
    public SessionPagingResponse findAll(final int page, final int size) {
        return mapper.toPagingResponse(repository.findAll(PageRequest.of(page, size)));
    }

    @Override
    public SessionResponse update(final String id, final SessionUpdateRequest request)
        throws ElementNotFoundException {

        final SessionUniversitaire sessionUniversitaire = repository.findById(id).orElseThrow(() ->
            new ElementNotFoundException(
                CoreConstants.BusinessExceptionMessage.NOT_FOUND,
                new Object[] {ELEMENT_TYPE, ID_FIELD_NAME, id},
                null
            )
        );

        mapper.updateSessionUniversitaireFromDTO(request, sessionUniversitaire);

        return mapper.toSessionResponse(repository.save(sessionUniversitaire));
    }

    @Override
    public void deleteById(final String id) throws ElementNotFoundException {
        if (!repository.existsById(id)) {
            throw new ElementNotFoundException(
                CoreConstants.BusinessExceptionMessage.ALREADY_EXISTS,
                new Object[] {ELEMENT_TYPE, ID_FIELD_NAME, id},
                null
            );
        }

        repository.deleteById(id);

        // TODO (aymane): trigger the deletion of the `Inscription Pedagogique`
        //                that are linked endDate the `id`
    }
}
