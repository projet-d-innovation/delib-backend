package ma.enset.noteservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.noteservice.client.ElementClient;
import ma.enset.noteservice.constant.CoreConstants;
import ma.enset.noteservice.dto.ElementResponse;
import ma.enset.noteservice.dto.GroupedElementsResponse;
import ma.enset.noteservice.dto.notemodule.NoteModuleCreationRequest;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    private final ElementClient elementClient;
    private final NoteModuleService noteModuleService;

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

        ElementResponse elementResponse = elementClient.get(noteElementCreationRequest.codeElement()).getBody();

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

        NoteElementResponse noteElementResponse = noteElementMapper.toNoteResponse(createdNoteElement);
        noteElementResponse.setElement(elementResponse);

        saveOrUpdateNoteModule(noteElementResponse);

        return noteElementResponse;

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

        List<ElementResponse> elementResponses = elementClient.getAllByIds(
                noteElementCreationRequests.stream()
                        .map(NoteElementCreationRequest::codeElement)
                        .collect(Collectors.toSet())
        ).getBody();

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

        List<NoteElementResponse> noteElementResponses = noteElementMapper.toNoteResponseList(
                noteElementRepository.saveAll(
                        noteElements
                )
        );

        noteElementResponses.forEach(
                noteElementResponse -> noteElementResponse.setElement(
                        elementResponses.stream()
                                .filter(
                                        elementResponse -> elementResponse.codeElement().equals(noteElementResponse.getCodeElement())
                                )
                                .findFirst()
                                .orElse(null)
                )
        );

        saveOrUpdateNoteModule(noteElementResponses);

        return noteElementResponses;
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

        NoteElementResponse updatedElementNote = noteElementMapper.toNoteResponse(
                noteElementRepository.save(noteElement)
        );

        updatedElementNote.setElement(
                elementClient.get(noteElementUpdateRequest.codeElement()).getBody()
        );

        saveOrUpdateNoteModule(updatedElementNote);

        return updatedElementNote;
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

        List<NoteElementResponse> updatedNoteElements = noteElementMapper.toNoteResponseList(
                noteElementRepository.saveAll(foundNoteElements)
        );

        List<ElementResponse> elementResponses = elementClient.getAllByIds(
                updatedNoteElements.stream()
                        .map(NoteElementResponse::getCodeElement)
                        .collect(Collectors.toSet())
        ).getBody();

        updatedNoteElements.forEach(
                noteElement -> noteElement.setElement(
                        elementResponses.stream()
                                .filter(
                                        elementResponse -> elementResponse.codeElement().equals(noteElement.getCodeElement())
                                )
                                .findFirst()
                                .orElse(null)
                )
        );

        saveOrUpdateNoteModule(updatedNoteElements);

        return updatedNoteElements;

    }

    @Override
    public GroupedNotesElementResponse getNotesBySession(String sessionId, boolean includeElement) throws ElementNotFoundException {

        // TODO (ahmed) : check if session exists

        final List<NoteElementResponse> noteElements = noteElementMapper.toNoteResponseList(
                noteElementRepository.findBySessionId(sessionId)
        );

        if (includeElement && !noteElements.isEmpty()) {
            Set<String> codeElementList = noteElements.stream()
                    .map(NoteElementResponse::getCodeElement)
                    .collect(Collectors.toSet());
            if (!codeElementList.isEmpty()) {
                final List<ElementResponse> elementResponses = elementClient.getAllByIds(codeElementList).getBody();

                if (elementResponses != null && !elementResponses.isEmpty()) {
                    noteElements.forEach(
                            noteElementResponse -> {
                                elementResponses.stream()
                                        .filter(elementResponse -> elementResponse.codeElement().equals(noteElementResponse.getCodeElement()))
                                        .findFirst()
                                        .ifPresent(noteElementResponse::setElement);
                            }
                    );
                }
            }
        }

        return GroupedNotesElementResponse.builder()
                .sessionId(sessionId)
                .notes(noteElements)
                .build();
    }

    @Override
    public Set<GroupedNotesElementResponse> getNotesBySessions(Set<String> sessionIdList, boolean includeElement) throws ElementNotFoundException {

        // TODO (ahmed) : check if sessions exists

        final List<NoteElementResponse> noteElementResponses = noteElementMapper.toNoteResponseList(
                noteElementRepository.findBySessionIdIn(sessionIdList)
        );


        if (includeElement && !noteElementResponses.isEmpty()) {
            Set<String> codeElementList = noteElementResponses.stream()
                    .map(NoteElementResponse::getCodeElement)
                    .collect(Collectors.toSet());
            if (!codeElementList.isEmpty()) {
                final List<ElementResponse> elementResponses = elementClient.getAllByIds(codeElementList).getBody();

                if (elementResponses != null && !elementResponses.isEmpty()) {
                    noteElementResponses.forEach(noteElementResponse -> {
                        elementResponses.forEach(elementResponse -> {
                            if (elementResponse.codeElement().equals(noteElementResponse.getCodeElement())) {
                                noteElementResponse.setElement(elementResponse);
                            }
                        });
                    });
                }
            }
        }

        final Map<String, List<NoteElementResponse>> groupedNotes =
                noteElementResponses.stream()
                        .collect(Collectors.groupingBy(NoteElementResponse::getSessionId));


        return groupedNotes.entrySet().stream()
                .map(
                        entry -> GroupedNotesElementResponse.builder()
                                .sessionId(entry.getKey())
                                .notes(entry.getValue())
                                .build()
                )
                .collect(Collectors.toSet());
    }

    @Override
    public void deleteBySessionAndElement(String sessionId, String codeElement) throws ElementNotFoundException {

        if (!noteElementRepository.existsById(
                NoteElement.NoteElementId.builder()
                        .sessionId(sessionId)
                        .codeElement(codeElement)
                        .build()
        )) {
            throw new ElementNotFoundException(
                    CoreConstants.BusinessExceptionMessage.NOT_FOUND,
                    new Object[]{ELEMENT_TYPE, ID_FIELD_NAME,
                            "(" + codeElement + "," + sessionId + ")"
                    },
                    null
            );
        }

        noteElementRepository.deleteBySessionIdAndCodeElement(sessionId, codeElement);

        // TODO (ahmed) : delete note module
    }

    @Override
    public void deleteAllBySessionAndElement(String sessionId, Set<String> codeElementList) throws ElementNotFoundException {

        final List<NoteElement> foundNoteElements = noteElementRepository.findBySessionIdAndCodeElementIn(sessionId, codeElementList);

        if (foundNoteElements.size() != codeElementList.size()) {
            throw new ElementNotFoundException(
                    CoreConstants.BusinessExceptionMessage.MANY_NOT_FOUND,
                    new Object[]{ELEMENT_TYPE, ID_FIELD_NAME},
                    codeElementList.stream()
                            .map(noteElementId -> "(" + noteElementId + "," + sessionId + ")")
                            .toList()

            );
        }

        noteElementRepository.deleteBySessionIdAndCodeElementIn(sessionId, codeElementList);

        // TODO (ahmed) : delete note module

    }

    @Override
    public void deleteBySession(String sessionId) throws ElementNotFoundException {

        List<NoteElement> foundNoteElements = noteElementRepository.findBySessionId(sessionId);

        if (foundNoteElements == null || foundNoteElements.isEmpty()) {
            throw new ElementNotFoundException(
                    CoreConstants.BusinessExceptionMessage.NOT_FOUND,
                    new Object[]{ELEMENT_TYPE, ID_FIELD_NAME,
                            "(x," + sessionId + ")"
                    },
                    null
            );
        }

        noteElementRepository.deleteBySessionId(sessionId);

        // TODO (ahmed) : delete note module

    }

    @Override
    public void deleteAllBySession(Set<String> sessionIdList) throws ElementNotFoundException {

        List<NoteElement> foundNoteElements = noteElementRepository.findBySessionIdIn(sessionIdList);

        if (foundNoteElements.size() != sessionIdList.size()) {
            throw new ElementNotFoundException(
                    CoreConstants.BusinessExceptionMessage.MANY_NOT_FOUND,
                    new Object[]{ELEMENT_TYPE, ID_FIELD_NAME},
                    sessionIdList.stream()
                            .map(sessionId -> "(x," + sessionId + ")")
                            .toList()

            );
        }

        noteElementRepository.deleteBySessionIdIn(sessionIdList);

        // TODO (ahmed) : delete note module

    }

    private void saveOrUpdateNoteModule(NoteElementResponse createdNoteElement) {

        List<ElementResponse> otherElements = elementClient.getAllByCodeModule(
                createdNoteElement.getElement().codeModule()
        ).getBody();

        if (otherElements == null || otherElements.isEmpty()) {
            return;
        }

        List<NoteElementResponse> noteElements = getNotesBySessionAndElements(
                createdNoteElement.getSessionId(),
                otherElements.stream()
                        .map(ElementResponse::codeElement)
                        .collect(Collectors.toSet())
        );

        if (noteElements == null || noteElements.isEmpty() || noteElements.size() != otherElements.size()) {
            return;
        }

        noteElements.forEach(
                noteElement -> {
                    noteElement.setElement(
                            otherElements.stream()
                                    .filter(
                                            elementResponse -> elementResponse.codeElement().equals(noteElement.getCodeElement())
                                    )
                                    .findFirst()
                                    .orElse(null)
                    );
                }
        );


        BigDecimal noteModule = noteElements.stream()
                .map(
                        noteElementResponse -> {
                            BigDecimal note = noteElementResponse.getNote();
                            BigDecimal coefficient = noteElementResponse.getElement().coefficientElement();
                            return note.multiply(coefficient).setScale(3, RoundingMode.DOWN);
                        }
                )
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(3, RoundingMode.DOWN);


        noteModuleService.saveOrUpdate(
                NoteModuleCreationRequest.builder()
                        .sessionId(createdNoteElement.getSessionId())
                        .codeModule(createdNoteElement.getElement().codeModule())
                        .note(noteModule)
                        .build()
        );
    }

    private void saveOrUpdateNoteModule(List<NoteElementResponse> createdNoteElements) {
        List<GroupedElementsResponse> otherElements = elementClient.getAllByCodesModule(
                createdNoteElements.stream()
                        .map(noteElementResponse -> noteElementResponse.getElement().codeModule())
                        .collect(Collectors.toSet())
        ).getBody();

        if (otherElements == null || otherElements.isEmpty()) {
            return;
        }

        List<NoteElementResponse> noteElements = getNotesBySessionAndElements(
                createdNoteElements.get(0).getSessionId(),
                otherElements.stream()
                        .flatMap(groupedElementsResponse -> groupedElementsResponse.elements().stream())
                        .map(ElementResponse::codeElement)
                        .collect(Collectors.toSet())
        );

//        if (noteElements == null || noteElements.isEmpty() || noteElements.size() != otherElements.size()) {
//            return;
//        }

        if (noteElements == null) {
            System.out.println("noteElements == null");
            return;
        }
        if (noteElements.isEmpty()) {
            System.out.println("noteElements.isEmpty()");
            return;
        }

        noteElements.forEach(
                noteElement -> {
                    noteElement.setElement(
                            otherElements.stream()
                                    .flatMap(groupedElementsResponse -> groupedElementsResponse.elements().stream())
                                    .filter(
                                            elementResponse -> elementResponse.codeElement().equals(noteElement.getCodeElement())
                                    )
                                    .findFirst()
                                    .orElse(null)
                    );
                }
        );

        Map<String, List<NoteElementResponse>> groupedNoteElementsByModule = noteElements.stream()
                .collect(
                        Collectors.groupingBy(
                                noteElementResponse -> noteElementResponse.getElement().codeModule()
                        )
                );

        groupedNoteElementsByModule.entrySet().removeIf(
                entry -> {
                    String codeModule = entry.getKey();
                    List<NoteElementResponse> noteElementResponseList = entry.getValue();
                    List<ElementResponse> elementResponseList = Objects.requireNonNull(otherElements.stream()
                                    .filter(
                                            groupedElementsResponse -> groupedElementsResponse.codeModule().equals(codeModule)
                                    )
                                    .findFirst()
                                    .orElse(null))
                            .elements();

                    return noteElementResponseList.size() != elementResponseList.size();
                }
        );

        List<NoteModuleCreationRequest> noteModuleCreationRequests = new ArrayList<>();

        groupedNoteElementsByModule.forEach(
                (codeModule, noteElementResponseList) -> {
                    BigDecimal noteModule = noteElementResponseList.stream()
                            .map(
                                    noteElementResponse -> {
                                        BigDecimal note = noteElementResponse.getNote();
                                        BigDecimal coefficient = noteElementResponse.getElement().coefficientElement();
                                        return note.multiply(coefficient).setScale(3, RoundingMode.DOWN);
                                    }
                            )
                            .reduce(BigDecimal.ZERO, BigDecimal::add)
                            .setScale(3, RoundingMode.DOWN);

                    noteModuleCreationRequests.add(
                            NoteModuleCreationRequest.builder()
                                    .sessionId(createdNoteElements.get(0).getSessionId())
                                    .codeModule(codeModule)
                                    .note(noteModule)
                                    .build()
                    );
                }
        );

        noteModuleService.saveOrUpdateAll(noteModuleCreationRequests);

    }

    private List<NoteElementResponse> getNotesBySessionAndElements(String sessionId, Set<String> codeElementList) throws ElementNotFoundException {
        return noteElementMapper.toNoteResponseList(
                noteElementRepository.findBySessionIdAndCodeElementIn(sessionId, codeElementList)
        );
    }


}
