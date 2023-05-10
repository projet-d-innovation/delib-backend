package ma.enset.noteservice.controller;

import lombok.AllArgsConstructor;
import ma.enset.noteservice.dto.ModuleResponse;
import ma.enset.noteservice.dto.NoteSemestreResponse;
import ma.enset.noteservice.service.NoteSemesterService;
import ma.enset.noteservice.util.NoteSemestreMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notes/semestres")
@AllArgsConstructor
@Validated
public class NoteSemestreController {
private final NoteSemesterService noteSemesterService;

//    @GetMapping("/{codeSemestre}/sessions/{codeSession}")
//    public ResponseEntity<NoteSemestreResponse> getNoteSemesterByCodeSession(@PathVariable("codeSemestre") String codeSemestre,@PathVariable("codeSession") String codesession) {
////        List<ModuleResponse> moduleResponseList =  moduleServiceFeignClient.getModuleByCodeSemestre(codeSemestre).getBody();          //  TODO: The endpoint of getModuleByCodeSemestre should be created in module-service
////        NoteSemestreResponse noteSemestreResponse = NoteSemestreMapper.toNoteSemestreResponse(codeSemestre, noteSemesterService.calculateNoteBySession(moduleResponseList, codesession));
//
//        return ResponseEntity
//                .ok()
//                .body(noteSemestreResponse);
//    }


}

