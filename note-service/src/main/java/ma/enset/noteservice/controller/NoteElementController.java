package ma.enset.noteservice.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;

import ma.enset.noteservice.constant.CoreConstants;
import ma.enset.noteservice.dto.NoteElementCreationRequest;
import ma.enset.noteservice.dto.NoteElementPagingResponse;
import ma.enset.noteservice.dto.NoteElementResponse;
import ma.enset.noteservice.dto.NoteElementUpdateRequest;
import ma.enset.noteservice.exception.ElementNotFoundException;
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

import java.util.List;

@RestController
@RequestMapping("/api/v1/notes/elements")
@AllArgsConstructor
@Validated
public class NoteElementController {
    private final NoteElementService noteElementService;
    private final NoteElementMapper noteElementMapper;

    @PostMapping
    public ResponseEntity<NoteElementResponse> save(@Valid @RequestBody NoteElementCreationRequest moduleCreationRequest) {
        NoteElement note = noteElementMapper.toModule(moduleCreationRequest);
        NoteElementResponse moduleResponse = noteElementMapper.toModuleResponse(noteElementService.save(note));
        if (note!= null){
            throw ElementNotFoundException.builder().key(CoreConstants.BusinessExceptionMessage.NOTE_MODULE_ALREADY_EXISTS)
                    .args(new Object[]{moduleResponse.NoteElementId()})
                    .build();
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(moduleResponse);
    }
//
    @PostMapping("/bulk")
    public ResponseEntity<List<NoteElementResponse>> saveAll(@RequestBody List<@Valid NoteElementCreationRequest> moduleCreationRequestList) {
        List<NoteElement> moduleList = noteElementMapper.toModuleList(moduleCreationRequestList);
        List<NoteElementResponse> moduleResponseList = noteElementMapper.toModuleResponseList(noteElementService.saveAll(moduleList));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(moduleResponseList);
    }
//
    @GetMapping("/{noteElementId}")
    public ResponseEntity<NoteElementResponse> get(@PathVariable("noteElementId") String noteElementId) {

        NoteElement foundModule = noteElementService.findById(noteElementId);
        NoteElementResponse foundModuleResponse = noteElementMapper.toModuleResponse(foundModule);

        return ResponseEntity
                .ok()
                .body(foundModuleResponse);
    }
//
    @GetMapping
    public ResponseEntity<NoteElementPagingResponse> getAll(@RequestParam(defaultValue = "0") @Min(0) int page,
                                                            @RequestParam(defaultValue = "10") @Range(min = 1, max = 10) int size) {

        Pageable pageRequest = PageRequest.of(page, size);
        Page<NoteElement> modulePage = noteElementService.findAll(pageRequest);

        NoteElementPagingResponse pagedResponse = noteElementMapper.toPagingResponse(modulePage);

        return ResponseEntity
                .ok()
                .body(pagedResponse);
    }

    @PatchMapping("/{noteElementId}")
    public ResponseEntity<NoteElementResponse> update(
        @PathVariable("noteElementId") String noteElementId,
        @Valid @RequestBody NoteElementUpdateRequest noteElementUpdateRequest
    ) {

        NoteElement module = noteElementService.findById(noteElementId);
        noteElementMapper.updateModuleFromDTO(noteElementUpdateRequest, module);

        NoteElement updatedModule = noteElementService.update(module);
        NoteElementResponse updatedModuleResponse = noteElementMapper.toModuleResponse(updatedModule);

        return ResponseEntity
                .ok()
                .body(updatedModuleResponse);
    }

    @DeleteMapping("/{noteElementId}")
    public ResponseEntity<?> delete(@PathVariable("noteElementId") String noteElementId) {
        noteElementService.deleteById(noteElementId);

        return ResponseEntity
                .noContent()
                .build();
    }

}

