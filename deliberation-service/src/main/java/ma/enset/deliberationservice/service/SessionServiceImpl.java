package ma.enset.deliberationservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.deliberationservice.constant.CoreConstants;
import ma.enset.deliberationservice.dto.session.SessionCreationRequest;
import ma.enset.deliberationservice.dto.session.SessionPagingResponse;
import ma.enset.deliberationservice.dto.session.SessionResponse;
import ma.enset.deliberationservice.dto.session.SessionUpdateRequest;
import ma.enset.deliberationservice.exception.DuplicateEntryException;
import ma.enset.deliberationservice.exception.ElementNotFoundException;
import ma.enset.deliberationservice.mapper.SessionMapper;
import ma.enset.deliberationservice.model.Session;
import ma.enset.deliberationservice.repository.SessionRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class SessionServiceImpl implements SessionService {
    private final String ELEMENT_TYPE = "Session";
    private final String ID_FIELD_NAME = "idSession";
    private final SessionMapper sessionMapper;
    private final SessionRepository sessionRepository;

    @Override
    public SessionResponse save(final SessionCreationRequest sessionCreationRequest) {
        SessionResponse sessionResponse = sessionMapper.toSessionResponse(
                sessionRepository.save(
                        sessionMapper.toSession(sessionCreationRequest)
                )
        );
        return sessionResponse;
    }

    @Override
    @Transactional
    public List<SessionResponse> saveAll(List<SessionCreationRequest> sessionResponseList) {

        List<Session> sessions = sessionMapper.toSessionList(sessionResponseList);

        List<Session> createdSessions;
        try {
            createdSessions = sessionRepository.saveAll(sessions);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEntryException(
                    CoreConstants.BusinessExceptionMessage.DUPLICATE_ENTRY,
                    null
            );
        }
        return sessionMapper.toSessionResponseList(createdSessions);
    }

    @Override
    public SessionResponse findById(String id) throws ElementNotFoundException {
        SessionResponse sessionResponse = sessionRepository.findById(id)
                .map(sessionMapper::toSessionResponse)
                .orElseThrow(() -> new ElementNotFoundException(
                        CoreConstants.BusinessExceptionMessage.NOT_FOUND,
                        new Object[]{ELEMENT_TYPE, ID_FIELD_NAME, id},
                        null
                ));
        return sessionResponse;
    }

    @Override
    public List<SessionResponse> findAllById(Set<String> ids) throws ElementNotFoundException {

        final List<Session> sessions = sessionRepository.findAllById(ids);

        List<String> notFoundIds = new ArrayList<>(ids);

        notFoundIds.removeAll(sessions.stream()
                .map(Session::getIdSession)
                .toList());

        if (!notFoundIds.isEmpty()) {
            throw new ElementNotFoundException(
                    CoreConstants.BusinessExceptionMessage.MANY_NOT_FOUND,
                    new Object[]{ELEMENT_TYPE},
                    notFoundIds
            );
        }

        List<SessionResponse> sessionResponses = sessionMapper.toSessionResponseList(sessions);

        return sessionResponses;
    }

    @Override
    public SessionPagingResponse findAll(int page, int size) {

        SessionPagingResponse sessionPagingResponse = sessionMapper.toPagingResponse(
                sessionRepository.findAll(
                        PageRequest.of(page, size)
                )
        );

        return sessionPagingResponse;
    }

    @Override
    public SessionResponse update(String id, SessionUpdateRequest sessionUpdateRequest) throws ElementNotFoundException {
        final Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException(
                        CoreConstants.BusinessExceptionMessage.NOT_FOUND,
                        new Object[]{ELEMENT_TYPE, ID_FIELD_NAME, id},
                        null
                ));

        sessionMapper.updateSessionFromDto(sessionUpdateRequest, session);

        return sessionMapper.toSessionResponse(sessionRepository.save(session));

    }

    @Override
    @Transactional
    public void deleteById(String id) throws ElementNotFoundException {
        if (!sessionRepository.existsById(id)) {
            throw new ElementNotFoundException(
                    CoreConstants.BusinessExceptionMessage.NOT_FOUND,
                    new Object[]{ELEMENT_TYPE, ID_FIELD_NAME, id},
                    null
            );
        }

        sessionRepository.deleteById(id);
    }


    @Override
    @Transactional
    public void deleteById(Set<String> ids) throws ElementNotFoundException {
        this.findAllById(ids);
        sessionRepository.deleteAllById(ids);
    }


    @Override
    public boolean existsAllId(Set<String> codesSession) throws ElementNotFoundException {

        List<String> foundSessionCodes = sessionRepository.findAllById(codesSession)
                .stream().map(Session::getIdSession).toList();

        if (codesSession.size() != foundSessionCodes.size()) {
            throw new ElementNotFoundException(
                    CoreConstants.BusinessExceptionMessage.MANY_NOT_FOUND,
                    new Object[]{ELEMENT_TYPE},
                    codesSession.stream()
                            .filter(code -> !foundSessionCodes.contains(code))
                            .toList()
            );
        }

        return true;
    }

}
