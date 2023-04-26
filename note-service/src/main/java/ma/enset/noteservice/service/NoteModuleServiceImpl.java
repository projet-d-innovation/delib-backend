package ma.enset.noteservice.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.noteservice.constant.CoreConstants;
import ma.enset.noteservice.dto.ElementResponse;
import ma.enset.noteservice.enums.Resultat;
import ma.enset.noteservice.exception.ElementAlreadyExistsException;
import ma.enset.noteservice.exception.ElementNotFoundException;

import ma.enset.noteservice.exception.InternalErrorException;
import ma.enset.noteservice.feign.ModuleServiceFeignClient;
import ma.enset.noteservice.model.NoteElement;
import ma.enset.noteservice.model.NoteModule;
import ma.enset.noteservice.repository.NoteModuleRepository;
import org.springframework.http.HttpStatus;
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

    @Override
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


    //
    @Override
    @Transactional
    public List<NoteModule> saveAll(List<NoteModule> noteModuleList) throws ElementAlreadyExistsException, InternalErrorException {

        List<NoteModule> createdNoteModule = new ArrayList<>(noteModuleList.size());
        noteModuleList.forEach(notemodule -> {
            if (moduleServiceFeignClient.getModuleByCode(notemodule.getCodeModule()).getStatusCode() != HttpStatus.OK)
                return;
            createdNoteModule.add(save(notemodule));
        });
        return createdNoteModule;
    }

    @Override
    public NoteModule findById(String codeModule) throws ElementNotFoundException {
        return noteModuleRepository.findById(codeModule)
                    .orElseThrow(() -> noteModuleNotFoundException(codeModule));
    }
//

    @Override
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
    public void deleteById(String noteModuleId) throws ElementNotFoundException {
        if (!noteModuleRepository.findById(noteModuleId).isPresent()) {
            throw noteModuleNotFoundException(noteModuleId);
        }

        noteModuleRepository.deleteById(noteModuleId);
    }

    @Override
    public List<NoteModule> findAllByCodeSession(String codeSession) {
           return noteModuleRepository.findByCodeSession(codeSession);
    }

    @Transactional
    @Override
    public List<NoteModule> updateAll(List<NoteModule> noteModuleList) throws ElementNotFoundException, InternalErrorException {
        List<NoteModule> updatedNoteModules = new ArrayList<>();
        noteModuleList.forEach(noteModule -> updatedNoteModules.add(update(noteModule)));
        return updatedNoteModules;
    }

    @Override
    public NoteModule findByCodeModuleAndCodeSession(String codeModule, String codeSession) {
        return noteModuleRepository.findByCodeModuleAndCodeSession(codeModule, codeSession);
    }

    @Override
    public NoteModule calculateNoteModule(NoteElement noteElement, String codeModule, List<ElementResponse> elementResponses, NoteElement noteElement1, NoteElement noteElement2) {
        NoteModule noteModule = this.findByCodeModuleAndCodeSession(codeModule, noteElement.getCodeSession());
      if(noteModule ==null){
          noteModule = new NoteModule();
          noteModule.setNoteModuleId(noteElement1.getNoteElementId()+ noteElement2.getNoteElementId());
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

    @Override
    public List<NoteModule> findAllByNoteModuleId(List<String> notModuleIdList) throws ElementNotFoundException {
        List<NoteModule> noteModuleList = new ArrayList<>();
        notModuleIdList.forEach(noteModuleId -> noteModuleList.add(this.findById(noteModuleId)));
        return noteModuleList;
    }

    private ElementNotFoundException noteModuleNotFoundException(String noteModuleId) {
        return ElementNotFoundException.builder()
                .key(CoreConstants.BusinessExceptionMessage.NOTE_MODULE_NOT_FOUND)
                .args(new Object[]{noteModuleId})
                .build();
    }

}
