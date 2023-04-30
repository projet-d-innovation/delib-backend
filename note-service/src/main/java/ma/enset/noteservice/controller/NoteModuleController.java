package ma.enset.noteservice.controller;


import lombok.AllArgsConstructor;
import ma.enset.noteservice.dto.*;
import ma.enset.noteservice.feign.ModuleServiceFeignClient;

import ma.enset.noteservice.model.NoteModule;
import ma.enset.noteservice.service.NoteModuleService;
import ma.enset.noteservice.util.NoteModuleMapper;
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
    private final ModuleServiceFeignClient moduleServiceFeignClient;

    @GetMapping("/{noteModuleId}")
    public ResponseEntity<NoteModuleWithModuleResponse> get(@PathVariable("noteModuleId") String noteModuleId) {
        NoteModule foundModule = noteModuleService.findById(noteModuleId);
        ResponseEntity<ModuleResponse> moduleresponse =  moduleServiceFeignClient.getModuleByCode(foundModule.getCodeModule());
        NoteModuleWithModuleResponse foundModuleResponse = noteModuleMapper.toNoteModuleWithModuleResponse(foundModule);
        foundModuleResponse = foundModuleResponse.setElementResponse(moduleresponse.getBody());
        return ResponseEntity
                .ok()
                .body(foundModuleResponse);
    }

    @GetMapping
    public ResponseEntity<List<NoteModuleResponse>> getAllBycodeSession(@RequestParam String codeSession) {

        List<NoteModule> noteModuleList = noteModuleService.findAllByCodeSession(codeSession);
        List<NoteModuleResponse> noteModuleResponses = noteModuleMapper.toNoteModuleResponseList(noteModuleList);

        return ResponseEntity
                .ok()
                .body(noteModuleResponses);
    }

}

