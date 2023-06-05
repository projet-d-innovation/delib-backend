package ma.enset.noteservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.noteservice.constant.CoreConstants;
import ma.enset.noteservice.dto.notemodule.GroupedNotesModuleResponse;
import ma.enset.noteservice.dto.notemodule.NoteModuleCreationRequest;
import ma.enset.noteservice.dto.notemodule.NoteModuleResponse;
import ma.enset.noteservice.exception.DuplicateEntryException;
import ma.enset.noteservice.exception.ElementAlreadyExistsException;
import ma.enset.noteservice.exception.ElementNotFoundException;
import ma.enset.noteservice.exception.InternalErrorException;
import ma.enset.noteservice.mapper.NoteModuleMapper;
import ma.enset.noteservice.model.NoteElement;
import ma.enset.noteservice.model.NoteModule;
import ma.enset.noteservice.repository.NoteModuleRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class NoteModuleServiceImpl implements NoteModuleService {
    private final String ELEMENT_TYPE = "NoteModule";
    private final String ID_FIELD_NAME = "ID(codeModule,sessionId)";
    private final NoteModuleMapper noteModuleMapper;
    private final NoteModuleRepository noteModuleRepository;

    @Override
    public NoteModuleResponse saveOrUpdate(final NoteModuleCreationRequest noteModuleCreationRequest) throws ElementAlreadyExistsException {

        System.out.println("from NoteModuleServiceImpl.saveOrUpdate");

        System.out.println("noteModuleCreationRequest.codeModule() = " + noteModuleCreationRequest.codeModule());
        System.out.println("noteModuleCreationRequest.sessionId() = " + noteModuleCreationRequest.sessionId());
        System.out.println("noteModuleCreationRequest.note() = " + noteModuleCreationRequest.note());
        System.out.println("noteModuleCreationRequest.redoublant() = " + noteModuleCreationRequest.redoublant());

        return null;
    }

    @Override
    @Transactional
    public List<NoteModuleResponse> saveOrUpdateAll(List<NoteModuleCreationRequest> noteModuleCreationRequests) throws ElementAlreadyExistsException {

        System.out.println("from NoteModuleServiceImpl.saveOrUpdateAll");


        for (int i = 0; i < noteModuleCreationRequests.size(); i++) {
            System.out.println("Entry number " + i + " :");
            System.out.println("noteModuleCreationRequests.get(i).codeModule() = " + noteModuleCreationRequests.get(i).codeModule());
            System.out.println("noteModuleCreationRequests.get(i).sessionId() = " + noteModuleCreationRequests.get(i).sessionId());
            System.out.println("noteModuleCreationRequests.get(i).note() = " + noteModuleCreationRequests.get(i).note());
            System.out.println("noteModuleCreationRequests.get(i).redoublant() = " + noteModuleCreationRequests.get(i).redoublant());
        }


        return null;

    }

    @Override
    public GroupedNotesModuleResponse getNotesBySession(String sessionId) throws ElementNotFoundException {

        return null;

    }

    @Override
    public Set<GroupedNotesModuleResponse> getNotesBySessions(Set<String> sessionIdList) throws ElementNotFoundException {

        return null;

    }

    @Override
    public void deleteBySessionAndElement(String sessionId, String codeElement) throws ElementNotFoundException {

    }

    @Override
    public void deleteAllBySessionAndElement(String sessionId, Set<String> codeElements) throws ElementNotFoundException {

    }

    @Override
    public void deleteBySession(String sessionId) throws ElementNotFoundException {

    }

    @Override
    public void deleteAllBySession(Set<String> sessionIdList) throws ElementNotFoundException {

    }
}
