package ma.enset.deliberationservice.service;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.deliberationservice.client.InscriptionClient;
import ma.enset.deliberationservice.client.NoteClient;
import ma.enset.deliberationservice.client.SemestreClient;
import ma.enset.deliberationservice.constant.CoreConstants;
import ma.enset.deliberationservice.dto.GroupedNotesModuleResponse;
import ma.enset.deliberationservice.dto.InscriptionResponse;
import ma.enset.deliberationservice.dto.RequiredSearchParams;
import ma.enset.deliberationservice.dto.session.SessionCreationRequest;
import ma.enset.deliberationservice.dto.session.SessionPagingResponse;
import ma.enset.deliberationservice.dto.session.SessionResponse;
import ma.enset.deliberationservice.dto.session.SessionUpdateRequest;
import ma.enset.deliberationservice.exception.DuplicateEntryException;
import ma.enset.deliberationservice.exception.ElementNotFoundException;
import ma.enset.deliberationservice.mapper.SessionMapper;
import ma.enset.deliberationservice.model.Session;
import ma.enset.deliberationservice.model.SessionType;
import ma.enset.deliberationservice.repository.SessionRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class SessionServiceImpl implements SessionService {
    private final String ELEMENT_TYPE = "Session";
    private final String ID_FIELD_NAME = "idSession";
    private final SessionMapper sessionMapper;
    private final SessionRepository sessionRepository;
    private final InscriptionClient inscriptionClient;
    private final SemestreClient semestreClient;
    private final NoteClient noteClient;

    @Override
    public SessionResponse save(final SessionCreationRequest sessionCreationRequest) {

        inscriptionClient.existAllByIds(
                Set.of(sessionCreationRequest.idInscription())
        );

        semestreClient.exists(
                Set.of(sessionCreationRequest.codeSemestre())
        );

        if (sessionCreationRequest.previousSessionId() != null) {
            if (!sessionRepository.existsById(
                    sessionCreationRequest.previousSessionId()
            )) {
                throw new ElementNotFoundException(
                        CoreConstants.BusinessExceptionMessage.NOT_FOUND,
                        new Object[]{ELEMENT_TYPE, ID_FIELD_NAME, sessionCreationRequest.previousSessionId()},
                        null
                );
            }
        }

        return sessionMapper.toSessionResponse(
                sessionRepository.save(
                        sessionMapper.toSession(sessionCreationRequest)
                )
        );
    }

    @Override
    @Transactional
    public List<SessionResponse> saveAll(List<SessionCreationRequest> sessionCreateRequest) {

        inscriptionClient.existAllByIds(
                sessionCreateRequest.stream()
                        .map(SessionCreationRequest::idInscription)
                        .collect(Collectors.toSet())
        );

        semestreClient.exists(
                sessionCreateRequest.stream()
                        .map(SessionCreationRequest::codeSemestre)
                        .collect(Collectors.toSet())
        );

        List<String> previousSessions = sessionCreateRequest.stream()
                .map(SessionCreationRequest::previousSessionId)
                .filter(Objects::nonNull)
                .toList();

        if (!previousSessions.isEmpty()) {
            List<Session> sessionsExists = sessionRepository.findAllById(
                    previousSessions
            );
            if (sessionsExists.size() != previousSessions.size()) {
                throw new ElementNotFoundException(
                        CoreConstants.BusinessExceptionMessage.MANY_NOT_FOUND,
                        new Object[]{ELEMENT_TYPE, ID_FIELD_NAME},
                        previousSessions
                );
            }
        }


        List<Session> sessions = sessionMapper.toSessionList(sessionCreateRequest);

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
        GroupedNotesModuleResponse notes = noteClient.getNotesBySession(
                sessionResponse.getIdSession(), true, true
        ).getBody();

        if (notes != null) {
            sessionResponse.setNotes(notes.notes());
        }

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

        includeNotes(sessionResponses);

        return sessionResponses;
    }


    // TODO (ahmed) : findAll should be removed | it's just for debugging
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
    public List<SessionResponse> search(
            String codeFiliere,
            String codeSemestre,
            String codeSessionUniversitaire,
            Integer annee,
            SessionType type
    ) {

        List<InscriptionResponse> inscriptions = inscriptionClient.getAllBySearchParams(
                codeFiliere,
                codeSessionUniversitaire,
                annee,
                true
        ).getBody();

        List<SessionResponse> sessionResponses = new ArrayList<>();

        if (inscriptions != null && !inscriptions.isEmpty()) {
            sessionResponses = sessionMapper.toSessionResponseList(
                    sessionRepository.findAllByIdInscriptionIn(
                            inscriptions.stream()
                                    .map(InscriptionResponse::id)
                                    .toList()
                    )

            );

            if (codeSemestre != null) {
                sessionResponses = sessionResponses.stream()
                        .filter(sessionResponse -> sessionResponse.getCodeSemestre().equals(codeSemestre))
                        .toList();
            }


            if (type != null) {
                sessionResponses = sessionResponses.stream()
                        .filter(sessionResponse -> sessionResponse.getSessionType().equals(type))
                        .toList();
            }

            includeNotes(sessionResponses);
        }

        if (!sessionResponses.isEmpty()) {
            sessionResponses.forEach(
                    sessionResponse -> {
                        sessionResponse.setInscription(
                                inscriptions.stream()
                                        .filter(inscriptionResponse -> inscriptionResponse.id().equals(sessionResponse.getIdInscription()))
                                        .findFirst()
                                        .orElse(null)
                        );
                    }
            );
        }


        return sessionResponses;
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

    private void includeNotes(List<SessionResponse> sessionResponses) {
        Set<String> sessionIds = sessionResponses.stream()
                .map(SessionResponse::getIdSession)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        if (!sessionIds.isEmpty()) {
            Set<GroupedNotesModuleResponse> notes = noteClient.getNotesBySessions(
                    sessionIds,
                    true,
                    true
            ).getBody();

            if (notes != null && !notes.isEmpty()) {
                sessionResponses.forEach(
                        sessionResponse -> {
                            Optional<GroupedNotesModuleResponse> optionalGroupedNotesModuleResponse = notes.stream()
                                    .filter(groupedNotesModuleResponse -> groupedNotesModuleResponse.sessionId().equals(sessionResponse.getIdSession()))
                                    .findFirst();
                            optionalGroupedNotesModuleResponse.ifPresent(groupedNotesModuleResponse -> sessionResponse.setNotes(groupedNotesModuleResponse.notes()));
                        }
                );
            }
        }
    }

}
