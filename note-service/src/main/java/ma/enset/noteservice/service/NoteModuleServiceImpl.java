package ma.enset.noteservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.noteservice.client.ElementClient;
import ma.enset.noteservice.client.ModuleClient;
import ma.enset.noteservice.client.SessionClient;
import ma.enset.noteservice.dto.ElementResponse;
import ma.enset.noteservice.dto.ModuleResponse;
import ma.enset.noteservice.dto.noteelement.NoteElementResponse;
import ma.enset.noteservice.dto.notemodule.GroupedNotesModuleResponse;
import ma.enset.noteservice.dto.notemodule.NoteModuleRequest;
import ma.enset.noteservice.dto.notemodule.NoteModuleResponse;
import ma.enset.noteservice.exception.ElementAlreadyExistsException;
import ma.enset.noteservice.exception.ElementNotFoundException;
import ma.enset.noteservice.mapper.NoteElementMapper;
import ma.enset.noteservice.mapper.NoteModuleMapper;
import ma.enset.noteservice.model.NoteModule;
import ma.enset.noteservice.repository.NoteElementRepository;
import ma.enset.noteservice.repository.NoteModuleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class NoteModuleServiceImpl implements NoteModuleService {
    private final String ELEMENT_TYPE = "NoteModule";
    private final String ID_FIELD_NAME = "ID(codeModule,sessionId)";
    private final NoteModuleMapper noteModuleMapper;
    private final NoteModuleRepository noteModuleRepository;
    private final NoteElementRepository noteElementRepository;
    private final NoteElementMapper noteElementMapper;
    private final ModuleClient moduleClient;
    private final ElementClient elementClient;
    private final SessionClient sessionClient;

    @Override
    public NoteModuleResponse saveOrUpdate(final NoteModuleRequest noteModuleRequest) throws ElementAlreadyExistsException {
        NoteModule noteModule = noteModuleRepository.findById(
                new NoteModule.NoteModuleId(
                        noteModuleRequest.codeModule(),
                        noteModuleRequest.sessionId()
                )
        ).orElse(null);

        if (noteModule != null) {
            noteModule.setNote(noteModuleRequest.note().floatValue());
        } else {
            noteModule = noteModuleMapper.toNote(noteModuleRequest);
        }

        NoteModuleResponse noteModuleResponse = noteModuleMapper.toNoteResponse(
                noteModuleRepository.save(noteModule)
        );

        noteModuleResponse.setModule(
                moduleClient.getById(noteModuleResponse.getCodeModule(), false).getBody()
        );

        return noteModuleResponse;
    }

    @Override
    @Transactional
    public List<NoteModuleResponse> saveOrUpdateAll(List<NoteModuleRequest> noteModuleRequests) throws ElementAlreadyExistsException {

        List<NoteModule> toBeUpdated = noteModuleRepository.findAllById(
                noteModuleRequests.stream().map(
                        noteModuleRequest -> new NoteModule.NoteModuleId(
                                noteModuleRequest.codeModule(),
                                noteModuleRequest.sessionId()
                        )
                ).collect(Collectors.toList())
        );

        List<NoteModule> toBeSaved = new ArrayList<>();

        for (NoteModuleRequest noteModuleRequest : noteModuleRequests) {
            boolean found = false;
            for (NoteModule noteModule : toBeUpdated) {
                if (noteModule.getCodeModule().equals(noteModuleRequest.codeModule()) && noteModule.getSessionId().equals(noteModuleRequest.sessionId())) {
                    noteModule.setNote(
                            noteModuleRequest.note().floatValue()
                    );
                    found = true;
                    break;
                }
            }
            if (!found) {
                toBeSaved.add(
                        noteModuleMapper.toNote(
                                noteModuleRequest
                        )
                );
            }
        }

        List<NoteModule> savedNoteModules = noteModuleRepository.saveAll(toBeSaved);

        savedNoteModules.addAll(
                noteModuleRepository.saveAll(toBeUpdated)
        );

        List<NoteModuleResponse> noteModuleResponses = noteModuleMapper.toNoteResponseList(savedNoteModules);

        Set<String> moduleCodes = noteModuleResponses.stream().map(
                        NoteModuleResponse::getCodeModule
                )
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        if (!moduleCodes.isEmpty()) {
            List<ModuleResponse> moduleResponses = moduleClient.getAllByIds(
                    moduleCodes,
                    false
            ).getBody();


            if (moduleResponses != null && !moduleResponses.isEmpty()) {
                noteModuleResponses.forEach(
                        noteModuleResponse -> noteModuleResponse.setModule(
                                moduleResponses.stream().filter(
                                        moduleResponse -> moduleResponse.getCodeModule().equals(noteModuleResponse.getCodeModule())
                                ).findFirst().orElse(null)
                        )
                );
            }
        }
        return noteModuleResponses;
    }

    @Override
    public GroupedNotesModuleResponse getNotesBySession(String sessionId, boolean includeModule, boolean includeNoteElement) throws ElementNotFoundException {

        sessionClient.existsAll(
                Set.of(sessionId)
        );

        List<NoteModuleResponse> noteModules = noteModuleMapper.toNoteResponseList(
                noteModuleRepository.findBySessionId(sessionId)
        );

        handleIncludes(Set.of(sessionId), noteModules, includeModule, includeNoteElement);

        return GroupedNotesModuleResponse.builder()
                .sessionId(sessionId)
                .notes(noteModules)
                .build();
    }

    @Override
    public Set<GroupedNotesModuleResponse> getNotesBySessions(Set<String> sessionIdList, boolean includeModule, boolean includeNoteElement) throws ElementNotFoundException {

        sessionClient.existsAll(sessionIdList);

        final List<NoteModuleResponse> noteModules = noteModuleMapper.toNoteResponseList(
                noteModuleRepository.findBySessionIdIn(sessionIdList)
        );

        handleIncludes(sessionIdList, noteModules, includeModule, includeNoteElement);

        return noteModules.stream().collect(
                Collectors.groupingBy(
                        NoteModuleResponse::getSessionId
                )
        ).entrySet().stream().map(
                entry -> GroupedNotesModuleResponse.builder()
                        .sessionId(entry.getKey())
                        .notes(entry.getValue())
                        .build()
        ).collect(Collectors.toSet());
    }

    @Override
    public void deleteBySessionAndModule(String sessionId, String codeModule) throws ElementNotFoundException {
        noteModuleRepository.deleteBySessionIdAndCodeModule(sessionId, codeModule);
    }

    @Override
    public void deleteAllBySessionAndModule(String sessionId, Set<String> codeModules) throws ElementNotFoundException {
        noteModuleRepository.deleteBySessionIdAndCodeModuleIn(sessionId, codeModules);
    }

    @Override
    public void deleteBySession(String sessionId) throws ElementNotFoundException {
        noteModuleRepository.deleteBySessionId(sessionId);
    }

    @Override
    public void deleteAllBySession(Set<String> sessionIdList) throws ElementNotFoundException {
        noteModuleRepository.deleteBySessionIdIn(sessionIdList);
    }


    private void handleIncludes(Set<String> sessionIds, List<NoteModuleResponse> noteModules, boolean includeModule, boolean includeNoteElement) {
        if (includeModule) {
            Set<String> moduleCodes = noteModules.stream().map(
                            NoteModuleResponse::getCodeModule
                    )
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            if (!moduleCodes.isEmpty()) {
                List<ModuleResponse> moduleResponses = moduleClient.getAllByIds(
                        moduleCodes,
                        false
                ).getBody();

                if (moduleResponses != null && !moduleResponses.isEmpty()) {
                    noteModules.forEach(
                            noteModuleResponse -> noteModuleResponse.setModule(
                                    moduleResponses.stream().filter(
                                            moduleResponse -> moduleResponse.getCodeModule().equals(noteModuleResponse.getCodeModule())
                                    ).findFirst().orElse(null)
                            )
                    );
                }
            }
        }

        if (includeNoteElement) {

            final List<NoteElementResponse> noteElements = noteElementMapper.toNoteResponseList(
                    noteElementRepository.findBySessionIdIn(
                            sessionIds
                    )
            );

            if (noteElements != null && !noteElements.isEmpty()) {
                List<ElementResponse> elementResponses = elementClient.getAllByIds(
                        noteElements.stream().map(
                                NoteElementResponse::getCodeElement
                        ).collect(Collectors.toSet())
                ).getBody();

                if (elementResponses != null && !elementResponses.isEmpty()) {
                    noteElements.forEach(
                            noteElementResponse -> noteElementResponse.setElement(
                                    elementResponses.stream().filter(
                                            elementResponse -> elementResponse.codeElement().equals(noteElementResponse.getCodeElement())
                                    ).findFirst().orElse(null)
                            )
                    );
                }
            }
            if (noteElements != null && !noteElements.isEmpty()) {
                Map<String, List<NoteElementResponse>> groupedNoteElements = noteElements.stream().collect(
                        Collectors.groupingBy(
                                noteElement -> noteElement.getSessionId() + noteElement.getElement().codeModule()
                        )
                );

                if (!groupedNoteElements.isEmpty()) {
                    noteModules.forEach(
                            noteModuleResponse -> noteModuleResponse.setNotesElement(
                                    groupedNoteElements.get(noteModuleResponse.getSessionId() + noteModuleResponse.getCodeModule())
                            )
                    );
                }
            }

        }
    }
}
