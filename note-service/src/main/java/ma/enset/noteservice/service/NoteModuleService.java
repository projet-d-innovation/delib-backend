package ma.enset.noteservice.service;

import ma.enset.noteservice.dto.ElementResponse;
import ma.enset.noteservice.dto.ModuleResponse;
import ma.enset.noteservice.dto.NoteElementUpdateRequest;
import ma.enset.noteservice.exception.ElementAlreadyExistsException;
import ma.enset.noteservice.exception.ElementNotFoundException;

import ma.enset.noteservice.exception.InternalErrorException;
import ma.enset.noteservice.model.NoteElement;
import ma.enset.noteservice.model.NoteModule;

import java.util.List;

public interface NoteModuleService {

    NoteModule checkElementsAndSave(NoteElement noteElement);

    List<NoteModule> checkElementsAndSaveAll(List<NoteElement> noteElementList);

    NoteModule checkElementsAndUpdate(NoteElement noteElement);

//    NoteModule findById(String noteModuleId) throws ElementNotFoundException;

    NoteModule findById(String codeModule) throws ElementNotFoundException;

    List<NoteModule> checkElementsAndUpdateAll(List<NoteElement> updatedNoteElementList);


    void delete(NoteModule notemodule) throws ElementNotFoundException;

    List<NoteModule> findAllByCodeSession(String codeSession);


    NoteModule findByCodeModuleAndCodeSession(String s, String codeSession);

    ModuleResponse getModule(String codeModule);
}
