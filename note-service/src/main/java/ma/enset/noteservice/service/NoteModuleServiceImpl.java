package ma.enset.noteservice.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.noteservice.constant.CoreConstants;
import ma.enset.noteservice.dto.ElementByCodeModuleResponse;
import ma.enset.noteservice.dto.ElementResponse;
import ma.enset.noteservice.enums.Resultat;
import ma.enset.noteservice.exception.ElementAlreadyExistsException;
import ma.enset.noteservice.exception.ElementNotFoundException;

import ma.enset.noteservice.exception.InternalErrorException;
import ma.enset.noteservice.feign.ElementServiceFeignClient;
import ma.enset.noteservice.feign.ModuleServiceFeignClient;
import ma.enset.noteservice.model.NoteElement;
import ma.enset.noteservice.model.NoteModule;
import ma.enset.noteservice.repository.NoteModuleRepository;
import ma.enset.noteservice.util.NoteElementMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
@AllArgsConstructor
@Slf4j
public class NoteModuleServiceImpl implements NoteModuleService {

    private final NoteModuleRepository noteModuleRepository;
    private final ModuleServiceFeignClient moduleServiceFeignClient;
    private final NoteElementService noteElementService;
    private final NoteElementMapper noteElementMapper;

    private final ElementServiceFeignClient elementServiceFeignClient;


    public NoteModule save(NoteModule noteModule) throws ElementAlreadyExistsException, InternalErrorException {
//        if (moduleServiceFeignClient.getModuleByCode(noteModule.getCodeModule()).getStatusCode() != HttpStatus.OK)
//            return null;

        if (noteModuleRepository.findById(noteModule.getNoteModuleId()).isPresent()) {
            throw ElementAlreadyExistsException.builder()
                    .key(CoreConstants.BusinessExceptionMessage.NOTE_MODULE_ALREADY_EXISTS)
                    .args(new Object[]{noteModule.getNoteModuleId()})
                    .build();
        }

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
        ResponseEntity<ElementResponse> elementResponse =  elementServiceFeignClient.getElementByCode(noteElement.getCodeElement());
        String codeModule = Objects.requireNonNull(elementResponse.getBody()).codeModule();
        ResponseEntity<ElementByCodeModuleResponse> elementResponseList =  elementServiceFeignClient.findByModule(codeModule);
        List<ElementResponse> elementResponses = Objects.requireNonNull(elementResponseList.getBody()).elements();
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


    //

    @Transactional
    public List<NoteModule> saveAll(List<NoteModule> noteModuleList) throws ElementAlreadyExistsException, InternalErrorException {

        List<NoteModule> createdNoteModule = new ArrayList<>(noteModuleList.size());
        noteModuleList.forEach(notemodule -> {
//            if (moduleServiceFeignClient.getModuleByCode(notemodule.getCodeModule()).getStatusCode() != HttpStatus.OK)
//                return;
            createdNoteModule.add(save(notemodule));
        });
        return createdNoteModule;
    }

    @Override
    public List<NoteModule> checkElementsAndSaveAll(List<NoteElement> noteElementList) {
        List<NoteModule> noteModuleList = new ArrayList<>();
        noteElementList.forEach(noteElement -> {
            ResponseEntity<ElementResponse> elementResponse =  elementServiceFeignClient.getElementByCode(noteElement.getCodeElement());
            String codeModule = Objects.requireNonNull(elementResponse.getBody()).codeModule();
            ResponseEntity<ElementByCodeModuleResponse> elementResponseList =  elementServiceFeignClient.findByModule(codeModule);
            List<ElementResponse> elementResponses = Objects.requireNonNull(elementResponseList.getBody()).elements();
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
        ResponseEntity<ElementResponse> elementResponse =  elementServiceFeignClient.getElementByCode(noteElement.getCodeElement());
        String codeModule = Objects.requireNonNull(elementResponse.getBody()).codeModule();
        ResponseEntity<ElementByCodeModuleResponse> elementResponseList =  elementServiceFeignClient.findByModule(codeModule);
        List<ElementResponse> elementResponses = Objects.requireNonNull(elementResponseList.getBody()).elements();
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
        ResponseEntity<ElementResponse> elementResponse =  elementServiceFeignClient.getElementByCode(noteElement.getCodeElement());
        String codeModule = Objects.requireNonNull(elementResponse.getBody()).codeModule();
        ResponseEntity<ElementByCodeModuleResponse> elementResponseList =  elementServiceFeignClient.findByModule(codeModule);
        List<ElementResponse> elementResponses = Objects.requireNonNull(elementResponseList.getBody()).elements();
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

//        if (moduleServiceFeignClient.getModuleByCode(noteModule.getCodeModule()).getStatusCode() != HttpStatus.OK)
//            return null;

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
           return noteModuleRepository.findByCodeSession(codeSession);
    }

    @Override
    public NoteModule findByCodeModuleAndCodeSession(String codeModule, String codeSession) {
        return noteModuleRepository.findByCodeModuleAndCodeSession(codeModule, codeSession);
    }

    @Transactional
    public List<NoteModule> updateAll(List<NoteModule> noteModuleList) throws ElementNotFoundException, InternalErrorException {
        List<NoteModule> updatedNoteModules = new ArrayList<>();
        noteModuleList.forEach(noteModule -> updatedNoteModules.add(update(noteModule)));
        return updatedNoteModules;
    }



    public NoteModule calculateNoteModule(NoteElement noteElement, String codeModule, List<ElementResponse> elementResponses, NoteElement noteElement1, NoteElement noteElement2) {
        NoteModule noteModule = this.findByCodeModuleAndCodeSession(codeModule, noteElement.getCodeSession());

      if(noteModule ==null){
          noteModule = new NoteModule();
          noteModule.setNoteModuleId(noteElement1.getNoteElementId()+ noteElement2.getCodeSession());
          noteModule.setCodeModule(codeModule);
          noteModule.setCodeSession(noteElement.getCodeSession());
          noteModule.setRedoublant(false);
          noteModule.setRattrapage(false);
          noteModule.setResultat(Resultat.VALIDE);
      }

        float noteModuleValue = (Objects.requireNonNull(elementResponses.get(0)).coefficientElement().floatValue() * noteElement1.getNote()+ Objects.requireNonNull(elementResponses.get(1)).coefficientElement().floatValue() * noteElement2.getNote()) / (elementResponses.get(0).coefficientElement().floatValue()+ elementResponses.get(1).coefficientElement().floatValue());
        noteModule.setNote(noteModuleValue);
        if (noteModuleValue < 12) {
            noteModule.setRattrapage(true);
            noteModule.setResultat(Resultat.NON_VALIDE);
        }
        if (noteElement1.isRedoublant() || noteElement2.isRedoublant()) {
            noteModule.setRedoublant(true);
        }
        return noteModule;
    }

    private ElementNotFoundException noteModuleNotFoundException(String noteModuleId) {
        return ElementNotFoundException.builder()
                .key(CoreConstants.BusinessExceptionMessage.NOTE_MODULE_NOT_FOUND)
                .args(new Object[]{noteModuleId})
                .build();
    }

}
