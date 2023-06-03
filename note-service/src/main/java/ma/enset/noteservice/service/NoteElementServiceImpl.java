package ma.enset.noteservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.noteservice.constant.CoreConstants;
import ma.enset.noteservice.exception.DuplicateEntryException;
import ma.enset.noteservice.exception.ElementAlreadyExistsException;
import ma.enset.noteservice.exception.ElementNotFoundException;
import ma.enset.noteservice.exception.InternalErrorException;
import ma.enset.noteservice.mapper.NoteElementMapper;
import ma.enset.noteservice.model.NoteElement;
import ma.enset.noteservice.repository.NoteElementRepository;
import ma.enset.noteservice.dto.noteelement.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class NoteElementServiceImpl implements NoteElementService {
    private final String ELEMENT_TYPE = "NoteElement";
    private final String ID_FIELD_NAME = "ID(codeElement,sessionId)";
    private final NoteElementMapper noteElementMapper;
    private final NoteElementRepository noteElementRepository;

    @Override
    public NoteElementResponse save(final NoteElementCreationRequest noteElementCreationRequest) throws ElementAlreadyExistsException {
        if (noteElementRepository.existsById(
                NoteElement.NoteElementId.builder()
                        .codeElement(noteElementCreationRequest.codeElement())
                        .sessionId(noteElementCreationRequest.sessionId())
                        .build()
        )) {
            throw new ElementAlreadyExistsException(
                    CoreConstants.BusinessExceptionMessage.ALREADY_EXISTS,
                    new Object[]{ELEMENT_TYPE, ID_FIELD_NAME,
                            "(" + noteElementCreationRequest.codeElement() + "," + noteElementCreationRequest.sessionId() + ")"
                    },
                    null
            );
        }

        // TODO (ahmed): check if session exists

        // TODO (ahmed): check if element exists

        NoteElement createdNoteElement = null;

        try {
            createdNoteElement = noteElementRepository.save(
                    noteElementMapper.toNote(noteElementCreationRequest)
            );
        } catch (DataIntegrityViolationException e) {
            throw new ElementAlreadyExistsException(
                    CoreConstants.BusinessExceptionMessage.ALREADY_EXISTS,
                    new Object[]{ELEMENT_TYPE, ID_FIELD_NAME,
                            "(" + noteElementCreationRequest.codeElement() + "," + noteElementCreationRequest.sessionId() + ")"
                    },
                    null
            );
        }

        return noteElementMapper.toNoteResponse(createdNoteElement);

    }

    @Override
    @Transactional
    public List<NoteElementResponse> saveAll(List<NoteElementCreationRequest> noteElementCreationRequests) throws ElementAlreadyExistsException {

        final List<NoteElement> foundNoteElements = noteElementRepository.findAllById(
                noteElementCreationRequests.stream()
                        .map(
                                noteElementCreationRequest -> NoteElement.NoteElementId.builder()
                                        .codeElement(noteElementCreationRequest.codeElement())
                                        .sessionId(noteElementCreationRequest.sessionId())
                                        .build()
                        )
                        .collect(Collectors.toSet())
        );
        if (!foundNoteElements.isEmpty()) {
            throw new ElementAlreadyExistsException(
                    CoreConstants.BusinessExceptionMessage.MANY_ALREADY_EXISTS,
                    new Object[]{ELEMENT_TYPE},
                    foundNoteElements.stream()
                            .map(NoteElement::getId)
                            .collect(Collectors.toList())

            );
        }


        // TODO (ahmed): check if sessions exists

        // TODO (ahmed): check if elements exists

        final List<NoteElement> noteElements = noteElementMapper.toNoteList(noteElementCreationRequests);

        Set<String> ids = noteElements.stream()
                .map(NoteElement::getId)
                .collect(Collectors.toSet());

        if (ids.size() != noteElements.size()) {
            throw new DuplicateEntryException(
                    CoreConstants.BusinessExceptionMessage.DUPLICATE_ENTRY,
                    null
            );
        }

        return noteElementMapper.toNoteResponseList(
                noteElementRepository.saveAll(
                        noteElements
                )
        );
    }

    @Override
    public NoteElementResponse update(NoteElementUpdateRequest noteElementUpdateRequest) throws ElementNotFoundException, InternalErrorException {

        final NoteElement noteElement = noteElementRepository.findById(
                        NoteElement.NoteElementId.builder()
                                .codeElement(noteElementUpdateRequest.codeElement())
                                .sessionId(noteElementUpdateRequest.sessionId())
                                .build()
                )
                .orElseThrow(() ->
                        new ElementNotFoundException(
                                CoreConstants.BusinessExceptionMessage.NOT_FOUND,
                                new Object[]{ELEMENT_TYPE, ID_FIELD_NAME,
                                        "(" + noteElementUpdateRequest.codeElement() + "," + noteElementUpdateRequest.sessionId() + ")"
                                },
                                null
                        ));

        noteElementMapper.updateNote(noteElementUpdateRequest, noteElement);

        return noteElementMapper.toNoteResponse(
                noteElementRepository.save(noteElement)
        );

    }

    @Override
    public List<NoteElementResponse> updateAll(Set<NoteElementUpdateRequest> noteElements) throws ElementNotFoundException {

        final Set<NoteElement.NoteElementId> ids = noteElements.stream()
                .map(
                        noteElementUpdateRequest -> NoteElement.NoteElementId.builder()
                                .codeElement(noteElementUpdateRequest.codeElement())
                                .sessionId(noteElementUpdateRequest.sessionId())
                                .build()
                )
                .collect(Collectors.toSet());

        final List<NoteElement> foundNoteElements = noteElementRepository.findAllById(ids);

        if (foundNoteElements.size() != ids.size()) {
            throw new ElementNotFoundException(
                    CoreConstants.BusinessExceptionMessage.MANY_NOT_FOUND,
                    new Object[]{ELEMENT_TYPE, ID_FIELD_NAME,
                            ids.stream()
                                    .map(noteElementId -> "(" + noteElementId + ")")
                                    .collect(Collectors.joining(","))
                    },
                    null
            );
        }

        noteElementMapper.updateNoteList(
                noteElements.stream().toList(),
                foundNoteElements
        );

        return noteElementMapper.toNoteResponseList(
                noteElementRepository.saveAll(foundNoteElements)
        );

    }

    @Override
    public GroupedNotesElementResponse getNotesBySession(String sessionId) throws ElementNotFoundException {

        // TODO (ahmed) : check if session exists

        final List<NoteElement> noteElements = noteElementRepository.findBySessionId(sessionId);

        return GroupedNotesElementResponse.builder()
                .sessionId(sessionId)
                .notes(noteElementMapper.toNoteResponseList(noteElements))
                .build();
    }

    @Override
    public Set<GroupedNotesElementResponse> getNotesBySessions(Set<String> sessionIdList) throws ElementNotFoundException {

        // TODO (ahmed) : check if sessions exists

        final List<NoteElement> noteElements = noteElementRepository.findBySessionIdIn(sessionIdList);

        final Map<String, List<NoteElement>> groupedNotes = noteElements.stream()
                .collect(Collectors.groupingBy(NoteElement::getSessionId));

        return groupedNotes.entrySet().stream()
                .map(
                        entry -> GroupedNotesElementResponse.builder()
                                .sessionId(entry.getKey())
                                .notes(noteElementMapper.toNoteResponseList(entry.getValue()))
                                .build()
                )
                .collect(Collectors.toSet());
    }
}
