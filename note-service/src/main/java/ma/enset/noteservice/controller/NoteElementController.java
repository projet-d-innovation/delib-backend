package ma.enset.noteservice.controller;

import jakarta.validation.Valid;

import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import ma.enset.noteservice.dto.*;
import ma.enset.noteservice.clients.ElementClient;
import ma.enset.noteservice.model.NoteElement;
import ma.enset.noteservice.model.NoteModule;
import ma.enset.noteservice.service.NoteElementService;
import ma.enset.noteservice.service.NoteModuleService;
import ma.enset.noteservice.util.NoteElementMapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/notes/elements")
@AllArgsConstructor
@Validated
@Slf4j
public class NoteElementController {
    private final NoteElementService noteElementService;
    private final NoteElementMapper noteElementMapper;
    private final NoteModuleService noteModuleService;
    private final ElementClient elementClient;

    @PostMapping
    public ResponseEntity<NoteElementResponse> save(@Valid @RequestBody NoteElementCreationRequest noteElementCreationRequest) {
        NoteElement noteElement = noteElementMapper.toNoteElement(noteElementCreationRequest);
        NoteElementResponse noteElementResponse = noteElementMapper.toNoteElementResponse(noteElementService.save(noteElement));

        NoteModule createdNotemodule = noteModuleService.checkElementsAndSave(noteElement);
        log.info("createdNotemodule: {}", createdNotemodule);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(noteElementResponse);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<NoteElementResponse>> saveAll(@RequestBody List<@Valid NoteElementCreationRequest> noteElementCreationRequestList) {

        List<NoteElement> noteElementList = noteElementMapper.toNoteElementList(noteElementCreationRequestList);
        List<NoteElementResponse> noteElementResponseList = noteElementMapper.toNoteElementResponseList(noteElementService.saveAll(noteElementList));

        List<NoteModule> createdNoteModuleList = noteModuleService.checkElementsAndSaveAll(noteElementList);
        log.info("createdNoteModuleList: {}", createdNoteModuleList);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(noteElementResponseList);
    }

    @GetMapping("/{noteElementId}")
    public ResponseEntity<NoteElementWithElementResponse> get(@PathVariable("noteElementId") String noteElementId) {

        NoteElement foundNoteElement = noteElementService.findById(noteElementId);
        ElementResponse elementResponse =  noteElementService.getElement(foundNoteElement.getCodeElement());
        NoteElementWithElementResponse foundModuleResponse = noteElementMapper.toNoteElementWithElementResponse(foundNoteElement);
        foundModuleResponse = foundModuleResponse.setElementResponse(elementResponse);
        return ResponseEntity
                .ok()
                .body(foundModuleResponse);
    }

    @GetMapping
    public  ResponseEntity< List<NoteElementWithElementResponse>> getAllByCodeSession(@RequestParam String codeSession) {

        List<NoteElement> noteElementList= noteElementService.findByCodeSession(codeSession);
        log.info("noteElementList: {}", noteElementList);
        List<String> codesElements = noteElementList.stream().distinct().map(NoteElement::getCodeElement).toList();
        List<ElementResponse> foundElements =  noteElementService.getElements(codesElements);
        List<NoteElementWithElementResponse> noteElementWithElementResponseList  = noteElementMapper.toNoteElementWithElementResponseList(noteElementList);
        noteElementWithElementResponseList.forEach(noteElementWithElementResponse -> {
            ElementResponse elementResponse = Objects.requireNonNull(foundElements).stream().filter(elementResponse1 -> elementResponse1.codeElement().equals(noteElementWithElementResponse.codeElement())).findFirst().get();
            noteElementWithElementResponse.setElementResponse(elementResponse);
        });

        return ResponseEntity
                .ok()
                .body(noteElementWithElementResponseList);
    }

    @PatchMapping("/{noteElementId}")
    public ResponseEntity<NoteElementResponse> update(
        @PathVariable("noteElementId") String noteElementId,
        @Valid @RequestBody NoteElementUpdateRequest noteElementUpdateRequest
    ) {
        NoteElement noteElement = noteElementService.findById(noteElementId);
        noteElementMapper.updateNoteElementFromDTO(noteElementUpdateRequest, noteElement);
        NoteElementResponse updatedModuleResponse = noteElementMapper.toNoteElementResponse(noteElementService.update(noteElement));

        NoteModule updatedNoteModule = noteModuleService.checkElementsAndUpdate(noteElement);
        log.info("updatedNoteModule : {}", updatedNoteModule);

        return ResponseEntity
                .ok()
                .body(updatedModuleResponse);
    }

    @PatchMapping("/bulk")
    public ResponseEntity<List<NoteElementResponse>> updateAll(
            @Valid @RequestBody List<NoteElementUpdateRequest> noteElementUpdateRequestList
    ) {
        List<NoteElement> noteElementList = noteElementService.findAllByNoteElementId(noteElementMapper.toNoteElementIdList(noteElementUpdateRequestList));
        noteElementMapper.updateNoteElementsFromDTO(noteElementUpdateRequestList, noteElementList);
        List<NoteElement> updatedNoteElementList = noteElementService.updateAll(noteElementList);
        List<NoteModule> updatedNoteModuleList =
        noteModuleService.checkElementsAndUpdateAll(updatedNoteElementList);
        log.info("updatedNoteModuleList : {}", updatedNoteModuleList);
        List<NoteElementResponse> noteElementResponseList = noteElementMapper.toNoteElementResponseList(updatedNoteElementList);
        return ResponseEntity
                .ok()
                .body(noteElementResponseList);
    }



    @DeleteMapping("/{noteElementId}")
    public ResponseEntity<?> delete(@PathVariable("noteElementId") String noteElementId) {
        NoteElement noteElement =  noteElementService.findById(noteElementId);
        noteElementService.deleteById(noteElementId);

        ElementResponse elementResponse =  noteElementService.getElement(noteElement.getCodeElement());
        NoteModule noteModule = noteModuleService.findByCodeModuleAndCodeSession(Objects.requireNonNull(elementResponse.codeModule()), noteElement.getCodeSession());
        noteModuleService.delete(noteModule);

        return ResponseEntity
                .noContent()
                .build();
    }

}

