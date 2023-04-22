package ma.enset.noteservice.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;

import ma.enset.noteservice.dto.*;
import ma.enset.noteservice.feign.ElementServiceFeignClient;
import ma.enset.noteservice.model.NoteElement;
import ma.enset.noteservice.service.NoteElementService;
import ma.enset.noteservice.util.NoteElementMapper;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/notes/elements")
@AllArgsConstructor
@Validated
public class NoteElementController {
    private final NoteElementService noteElementService;
    private final NoteElementMapper noteElementMapper;
    private final ElementServiceFeignClient elementServiceFeignClient;

    @PostMapping
    public ResponseEntity<NoteElementResponse> save(@Valid @RequestBody NoteElementCreationRequest noteElementCreationRequest) {
        NoteElement note = noteElementMapper.toNoteElement(noteElementCreationRequest);
        NoteElementResponse noteElementResponse = noteElementMapper.toNoteElementResponse(noteElementService.save(note));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(noteElementResponse);
    }
//
    @PostMapping("/bulk")
    public ResponseEntity<List<NoteElementResponse>> saveAll(@RequestBody List<@Valid NoteElementCreationRequest> noteElementCreationRequestList) {
        List<NoteElement> noteElementList = noteElementMapper.toNoteElementList(noteElementCreationRequestList);
        List<NoteElementResponse> noteElementResponseList = noteElementMapper.toNoteElementResponseList(noteElementService.saveAll(noteElementList));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(noteElementResponseList);
    }

    @GetMapping("/{noteElementId}")
    public ResponseEntity<NoteElementWithElementResponse> get(@PathVariable("noteElementId") String noteElementId) {

        NoteElement foundNoteElement = noteElementService.findById(noteElementId);
        ResponseEntity<ElementResponse> elementResponse =  elementServiceFeignClient.getElementByCode(foundNoteElement.getCodeElement());
        NoteElementWithElementResponse foundModuleResponse = noteElementMapper.toNoteElementWithElementResponse(foundNoteElement);
        foundModuleResponse = foundModuleResponse.setElementResponse(elementResponse.getBody());
        return ResponseEntity
                .ok()
                .body(foundModuleResponse);
    }
    @GetMapping
    public  ResponseEntity< List<NoteElementWithElementResponse>> getAllByCodeSession(@RequestParam String codeSession) {

//        Pageable pageRequest = PageRequest.of(page, size);
        List<NoteElement> noteElementList= noteElementService.findByCodeSession(codeSession);
        List<String> codesElements = noteElementList.stream().distinct().map(NoteElement::getCodeElement).toList();
        ResponseEntity<List<ElementResponse>> foundElements =  elementServiceFeignClient.findByCodeElements11(codesElements);
//        NoteElementPagingResponse pagedResponse = noteElementMapper.toPagingResponse(noteElementPage);
        List<NoteElementWithElementResponse> noteElementWithElementResponseList  = noteElementMapper.toNoteElementWithElementResponseList(noteElementList);
         noteElementWithElementResponseList.forEach(noteElementWithElementResponse -> {
             ElementResponse elementResponse = Objects.requireNonNull(foundElements.getBody()).stream().filter(elementResponse1 -> elementResponse1.codeElement().equals(noteElementWithElementResponse.codeElement())).findFirst().get();
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
        NoteElement updatedModule = noteElementService.update(noteElement);
        NoteElementResponse updatedModuleResponse = noteElementMapper.toNoteElementResponse(updatedModule);

        return ResponseEntity
                .ok()
                .body(updatedModuleResponse);
    }

    @PatchMapping("/bulk")
    public ResponseEntity<List<NoteElementResponse>> updateMany(
            @NotEmpty @RequestParam("noteElementId") List<String> noteElementIds,
            @Valid @RequestBody NoteElementUpdateRequest noteElementUpdateRequest
    ) {
        List<NoteElementResponse> updatedModuleResponseList = new ArrayList<>();
        noteElementIds.forEach(noteElementId ->{
            NoteElement noteElement = noteElementService.findById(noteElementId);
            noteElementMapper.updateNoteElementFromDTO(noteElementUpdateRequest, noteElement);
            NoteElement updatedModule = noteElementService.update(noteElement);
        NoteElementResponse updatedModuleResponse = noteElementMapper.toNoteElementResponse(updatedModule);
            updatedModuleResponseList.add(updatedModuleResponse);
        });

        return ResponseEntity
                .ok()
                .body(updatedModuleResponseList);
    }



    @DeleteMapping("/{noteElementId}")
    public ResponseEntity<?> delete(@PathVariable("noteElementId") String noteElementId) {
        noteElementService.deleteById(noteElementId);

        return ResponseEntity
                .noContent()
                .build();
    }

}

