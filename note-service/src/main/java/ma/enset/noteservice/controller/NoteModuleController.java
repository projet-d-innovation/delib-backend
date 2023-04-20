package ma.enset.noteservice.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import ma.enset.noteservice.dto.*;
import ma.enset.noteservice.model.NoteModule;
import ma.enset.noteservice.service.NoteModuleService;
import ma.enset.noteservice.util.NoteModuleMapper;
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
@RequestMapping("/api/v1/notes/modules")
@AllArgsConstructor
@Validated
public class NoteModuleController {
    private final NoteModuleService noteModuleService;
    private final NoteModuleMapper noteModuleMapper;

    @PostMapping
    public ResponseEntity<NoteModuleResponse> save(@Valid @RequestBody NoteModuleCreationRequest noteModuleCreationRequest) {
        NoteModule noteModule = noteModuleMapper.toModule(noteModuleCreationRequest);
        NoteModuleResponse noteModuleResponse = noteModuleMapper.toModuleResponse(noteModuleService.save(noteModule));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(noteModuleResponse);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<NoteModuleResponse>> saveAll(@RequestBody List<@Valid NoteModuleCreationRequest> noteModuleCreationRequestList) {
        List<NoteModule> moduleList = noteModuleMapper.toModuleList(noteModuleCreationRequestList);
        List<NoteModuleResponse> moduleResponseList = noteModuleMapper.toModuleResponseList(noteModuleService.saveAll(moduleList));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(moduleResponseList);
    }
////
    @GetMapping("/{noteModuleId}")
    public ResponseEntity<NoteModuleResponse> get(@PathVariable("noteModuleId") String noteModuleId) {

        NoteModule foundModule = noteModuleService.findById(noteModuleId);
        NoteModuleResponse foundModuleResponse = noteModuleMapper.toModuleResponse(foundModule);

        return ResponseEntity
                .ok()
                .body(foundModuleResponse);
    }
//
    @GetMapping
    public ResponseEntity<NoteModulePagingResponse> getAll(@RequestParam(defaultValue = "0") @Min(0) int page,
                                                            @RequestParam(defaultValue = "10") @Range(min = 1, max = 10) int size) {

        Pageable pageRequest = PageRequest.of(page, size);
        Page<NoteModule> modulePage = noteModuleService.findAll(pageRequest);
        NoteModulePagingResponse pagedResponse = noteModuleMapper.toPagingResponse(modulePage);

        return ResponseEntity
                .ok()
                .body(pagedResponse);
    }
//
    @PatchMapping("/{noteModuleId}")
    public ResponseEntity<NoteModuleResponse> update(
        @PathVariable("noteModuleId") String noteModuleId,
        @Valid @RequestBody NoteModuleUpdateRequest noteModuleUpdateRequest
    ) {

        NoteModule module = noteModuleService.findById(noteModuleId);
        noteModuleMapper.updateModuleFromDTO(noteModuleUpdateRequest, module);

        NoteModule updatedModule = noteModuleService.update(module);
        NoteModuleResponse updatedModuleResponse = noteModuleMapper.toModuleResponse(updatedModule);

        return ResponseEntity
                .ok()
                .body(updatedModuleResponse);
    }

    @DeleteMapping("/{noteModuleId}")
    public ResponseEntity<?> delete(@PathVariable("noteModuleId") String noteModuleId) {
        noteModuleService.deleteById(noteModuleId);

        return ResponseEntity
                .noContent()
                .build();
    }

}

