package ma.enset.noteservice.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.noteservice.constant.CoreConstants;
import ma.enset.noteservice.dto.ElementByCodeModuleResponse;
import ma.enset.noteservice.dto.ElementResponse;
import ma.enset.noteservice.dto.ModuleResponse;
import ma.enset.noteservice.enums.Resultat;
import ma.enset.noteservice.exception.ElementAlreadyExistsException;
import ma.enset.noteservice.exception.ElementNotFoundException;

import ma.enset.noteservice.exception.ExchangerException;
import ma.enset.noteservice.exception.InternalErrorException;
import ma.enset.noteservice.clients.ModuleClient;
import ma.enset.noteservice.model.NoteElement;
import ma.enset.noteservice.model.NoteModule;
import ma.enset.noteservice.repository.NoteModuleRepository;
import ma.enset.noteservice.util.NoteElementMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
@AllArgsConstructor
@Slf4j
public class NoteModuleServiceImpl implements NoteModuleService {

    private final NoteModuleRepository noteModuleRepository;
    private final NoteElementService noteElementService;
    private final NoteElementMapper noteElementMapper;
    private final ModuleClient moduleClient;

    public NoteModule save(NoteModule noteModule) throws ElementAlreadyExistsException, InternalErrorException {

        if (noteModuleRepository.findById(noteModule.getNoteModuleId()).isPresent()) {
            throw ElementAlreadyExistsException.builder()
                    .key(CoreConstants.BusinessExceptionMessage.NOTE_MODULE_ALREADY_EXISTS)
                    .args(new Object[]{noteModule.getNoteModuleId()})
                    .build();
        }

        getModule(noteModule.getCodeModule());

        NoteModule createNoteModule = null;

        try {
            createNoteModule = noteModuleRepository.save(noteModule);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            throw new InternalErrorException();
        }

        return createNoteModule;
    }

    @Override
    public NoteModule checkElementsAndSave(NoteElement noteElement) {
        ElementResponse elementResponse =  noteElementService.getElement(noteElement.getCodeElement());
        String codeModule = Objects.requireNonNull(elementResponse).codeModule();
        ElementByCodeModuleResponse elementResponseList =  noteElementService.getElementByModule(codeModule);
        List<ElementResponse> elementResponses = Objects.requireNonNull(elementResponseList).elements();
        List<String> codeElements = noteElementMapper.toCodeElementList(elementResponses);
        NoteElement noteElement1 =  noteElementService.findByCodeSessionAndCodeElement(noteElement.getCodeSession(), codeElements.get(0));
        NoteElement noteElement2 =  noteElementService.findByCodeSessionAndCodeElement(noteElement.getCodeSession(), codeElements.get(1));

        log.info("noteElement1 : {}", noteElement1);
        log.info("noteElement2 : {}", noteElement2);
        NoteModule createdNoteModule = null;
        if (noteElement1 != null && noteElement2 != null) {
            createdNoteModule = this.save(this.calculateNoteModule(noteElement, codeModule, elementResponses, noteElement1, noteElement2));
        }
        return  createdNoteModule;
    }



    @Transactional
    public List<NoteModule> saveAll(List<NoteModule> noteModuleList) throws ElementAlreadyExistsException, InternalErrorException {

        noteModuleList.forEach(notemodule -> {
            if (noteModuleRepository.findById(notemodule.getNoteModuleId()).isPresent()) {
                throw ElementAlreadyExistsException.builder()
                        .key(CoreConstants.BusinessExceptionMessage.NOTE_ELEMENT_ALREADY_EXISTS)
                        .args(new Object[]{notemodule.getNoteModuleId()})
                        .build();
            }
        });

//      getElements(noteElementList.stream().map(NoteElement::getCodeElement).toList());   TODO: implement this in module service
//      getSession(codeSession);  TODO: implement this in deliberation service


        List<NoteModule> savedNoteModules = new ArrayList<>(noteModuleList.size());
        try {
            savedNoteModules = noteModuleRepository.saveAll(noteModuleList);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            throw new InternalErrorException();
        }
        return noteModuleList;
    }

    @Override
    public List<NoteModule> checkElementsAndSaveAll(List<NoteElement> noteElementList) {
        List<NoteModule> noteModuleList = new ArrayList<>();
        noteElementList.forEach(noteElement -> {
            ElementResponse elementResponse =  noteElementService.getElement(noteElement.getCodeElement());
            String codeModule = Objects.requireNonNull(elementResponse).codeModule();
            ElementByCodeModuleResponse elementResponseList =  noteElementService.getElementByModule(codeModule);
            List<ElementResponse> elementResponses = Objects.requireNonNull(elementResponseList).elements();
            List<String> codeElements = noteElementMapper.toCodeElementList(elementResponses);
            NoteElement noteElement1 =  noteElementService.findByCodeSessionAndCodeElement(noteElement.getCodeSession(), codeElements.get(0));
            NoteElement noteElement2 =  noteElementService.findByCodeSessionAndCodeElement(noteElement.getCodeSession(), codeElements.get(1));

            log.info("noteElement1 : {}", noteElement1);
            log.info("noteElement2 : {}", noteElement2);
            if (noteElement1 != null && noteElement2 != null) {
                NoteModule noteModule = this.calculateNoteModule(noteElement, codeModule, elementResponses, noteElement1, noteElement2);
                if (!noteModuleList.contains(noteModule))
                    noteModuleList.add(noteModule);
            }
        });
        List<NoteModule> createdNoteModuleList = new ArrayList<>(noteModuleList.size());
        if (!noteModuleList.isEmpty()) {
            createdNoteModuleList = this.saveAll(noteModuleList);
        }
        return createdNoteModuleList;
    }

    @Override
    public NoteModule checkElementsAndUpdate(NoteElement noteElement) {
        ElementResponse elementResponse =  noteElementService.getElement(noteElement.getCodeElement());
        String codeModule = Objects.requireNonNull(elementResponse).codeModule();
        ElementByCodeModuleResponse elementResponseList =  noteElementService.getElementByModule(codeModule);
        List<ElementResponse> elementResponses = Objects.requireNonNull(elementResponseList).elements();
        List<String> codeElements = noteElementMapper.toCodeElementList(elementResponses);
        NoteElement noteElement1 =  noteElementService.findByCodeSessionAndCodeElement(noteElement.getCodeSession(), codeElements.get(0));
        NoteElement noteElement2 =  noteElementService.findByCodeSessionAndCodeElement(noteElement.getCodeSession(), codeElements.get(1));

        log.info("noteElement1 : {}", noteElement1);
        log.info("noteElement2 : {}", noteElement2);
        NoteModule updatedNoteModule = null;
        if (noteElement1 != null && noteElement2 != null) {
             updatedNoteModule = this.update(this.calculateNoteModule(noteElement, codeModule, elementResponses, noteElement1, noteElement2));
        }
       return updatedNoteModule;
    }
    @Override
    public NoteModule findById(String codeModule) throws ElementNotFoundException {
        return noteModuleRepository.findById(codeModule)
                    .orElseThrow(() -> noteModuleNotFoundException(codeModule));
    }
//
    @Override
    public List<NoteModule> checkElementsAndUpdateAll(List<NoteElement> updatedNoteElementList) {
    List<NoteModule> noteModuleList = new ArrayList<>();
        updatedNoteElementList.forEach(noteElement -> {
        ElementResponse elementResponse =  noteElementService.getElement(noteElement.getCodeElement());
        String codeModule = Objects.requireNonNull(elementResponse).codeModule();
        ElementByCodeModuleResponse elementResponseList =  noteElementService.getElementByModule(codeModule);
        List<ElementResponse> elementResponses = Objects.requireNonNull(elementResponseList).elements();
        List<String> codeElements = noteElementMapper.toCodeElementList(elementResponses);
        NoteElement noteElement1 =  noteElementService.findByCodeSessionAndCodeElement(noteElement.getCodeSession(), codeElements.get(0));
        NoteElement noteElement2 =  noteElementService.findByCodeSessionAndCodeElement(noteElement.getCodeSession(), codeElements.get(1));

            log.info("noteElement1 : {}", noteElement1);
            log.info("noteElement2 : {}", noteElement2);
        if (noteElement1 != null && noteElement2 != null) {
            noteModuleList.add(this.calculateNoteModule(noteElement, codeModule, elementResponses, noteElement1, noteElement2));
            }
        });
        List<NoteModule> updatedNoteModule = new ArrayList<>(noteModuleList.size());
        if (!noteModuleList.isEmpty()) {
            updatedNoteModule= this.updateAll(noteModuleList);
        }
        return updatedNoteModule;
    }

    public NoteModule update(NoteModule noteModule) throws ElementNotFoundException, InternalErrorException {


        if (!noteModuleRepository.findById(noteModule.getNoteModuleId()).isPresent()) {
            throw noteModuleNotFoundException(noteModule.getNoteModuleId());
        }

        NoteModule updatedNoteModule = null;

        try {
            updatedNoteModule = noteModuleRepository.save(noteModule);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            throw new InternalErrorException();
        }

        return updatedNoteModule;
    }
    @Override
    public void delete(NoteModule notemodule) throws ElementNotFoundException {
        if (!noteModuleRepository.findById(notemodule.getNoteModuleId()).isPresent()) {
            throw noteModuleNotFoundException(notemodule.getNoteModuleId());
        }
        noteModuleRepository.deleteById(notemodule.getNoteModuleId());
    }

    @Override
    public List<NoteModule> findAllByCodeSession(String codeSession) {
//        getSession(codeSession);  TODO: implement this in deliberation service
        return noteModuleRepository.findByCodeSession(codeSession);
    }

    @Override
    public NoteModule findByCodeModuleAndCodeSession(String codeModule, String codeSession) {
//        getSession(codeSession);  TODO: implement this in deliberation service
        return noteModuleRepository.findByCodeModuleAndCodeSession(codeModule, codeSession);
    }

    @Transactional
    public List<NoteModule> updateAll(List<NoteModule> noteModuleList) throws ElementNotFoundException, InternalErrorException {

        noteModuleList.forEach(noteModule -> {
            if (!noteModuleRepository.findById(noteModule.getNoteModuleId()).isPresent()) {
                throw noteModuleNotFoundException(noteModule.getNoteModuleId());
            }
        });
        List<NoteModule> updatedNotemodule = new ArrayList<>();

        try {
            updatedNotemodule = noteModuleRepository.saveAll(noteModuleList);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            throw new InternalErrorException();
        }
        return updatedNotemodule;
    }



    public NoteModule calculateNoteModule(NoteElement noteElement, String codeModule, List<ElementResponse> elementResponses, NoteElement noteElement1, NoteElement noteElement2) {
        NoteModule currentNoteModule = this.findByCodeModuleAndCodeSession(codeModule, noteElement.getCodeSession());

        if (currentNoteModule == null) {
            currentNoteModule = new NoteModule();
            currentNoteModule.setNoteModuleId(noteElement1.getNoteElementId() + noteElement2.getCodeSession());
            currentNoteModule.setCodeModule(codeModule);
            currentNoteModule.setCodeSession(noteElement.getCodeSession());
            currentNoteModule.setRedoublant(false);
            currentNoteModule.setRattrapage(true);
            currentNoteModule.setResultat(Resultat.NON_VALIDE);
        }
        float CurrentNoteModuleValue = (Objects.requireNonNull(elementResponses.get(0)).coefficientElement().floatValue() * noteElement1.getNote() + Objects.requireNonNull(elementResponses.get(1)).coefficientElement().floatValue() * noteElement2.getNote()) / (elementResponses.get(0).coefficientElement().floatValue() + elementResponses.get(1).coefficientElement().floatValue());
        currentNoteModule.setNote(CurrentNoteModuleValue);


//        TODO: for the rattrapage session
//        SessionResponse session = sessionServiceFeignClient.findSessionByCodeSession(noteElement.getCodeSession())   TODO: should get the current session first
//        if (session.type == TypeSession.RATRAPAGE) {                                                                     TODO: check the session if it's rattrapage
//            NoteModule previusNoteModule = this.findByCodeModuleAndCodeSession(codeModule, session.getNormalCodeSession());   TODO: get the note module of the normal session
//            CurrentNoteModuleValue = Math.min(12, Math.max(CurrentNoteModuleValue, previusNoteModule.getNote()))            TODO: calculate the note module
//            currentNoteModule.setNote(CurrentNoteModuleValue);
//         if (CurrentNoteModuleValue >= 12 && (noteElement1.getNote() >= 6 && noteElement2.getNote() >= 6)) {  TODO: check if the note module is valid
//            currentNoteModule.setResultat(Resultat.VALIDE);
//        }
//        if (noteElement1.isRedoublant() || noteElement2.isRedoublant()) {                                     TODO: check if the module is redoublant
//            currentNoteModule.setRedoublant(true);
//        }
//        }else{

//        TODO: for the normal session
        if (CurrentNoteModuleValue >= 12 && (noteElement1.getNote() >= 6 && noteElement2.getNote() >= 6)) {
            currentNoteModule.setRattrapage(false);
            currentNoteModule.setResultat(Resultat.VALIDE);
        }
        if (noteElement1.isRedoublant() || noteElement2.isRedoublant()) {
            currentNoteModule.setRedoublant(true);
        }
//       }
        return currentNoteModule;
    }
    @Override
    public ModuleResponse getModule(String codeModule) {
        ModuleResponse moduleResponse = null;
        try {
            moduleResponse = moduleClient.getModule(codeModule).getBody();
            log.info("elementResponse: {}", moduleResponse);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw ExchangerException.builder()
                    .exceptionBody(e.getResponseBodyAsString())
                    .build();
        }
        return moduleResponse;
    }


//    @Override
//    public List<ElementResponse> getModules(List<String> codeElements) throws ExchangerException{
//        List<ElementResponse> moduleresponses = new ArrayList<>();
//        try {
//            moduleresponses = moduleClient.getModules(codeElements).getBody(); TODO: implement this in module service
//        } catch (HttpClientErrorException | HttpServerErrorException e) {
//            throw ExchangerException.builder()
//                    .exceptionBody(e.getResponseBodyAsString())
//                    .build();
//        }
//        return moduleresponse;
//    }


    private ElementNotFoundException noteModuleNotFoundException(String noteModuleId) {
        return ElementNotFoundException.builder()
                .key(CoreConstants.BusinessExceptionMessage.NOTE_MODULE_NOT_FOUND)
                .args(new Object[]{noteModuleId})
                .build();
    }

}
