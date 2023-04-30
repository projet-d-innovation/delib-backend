package ma.enset.noteservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.noteservice.dto.ModuleResponse;
import ma.enset.noteservice.model.NoteModule;
import ma.enset.noteservice.util.NoteModuleMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
@AllArgsConstructor
@Slf4j
public class NoteSemestreServiceImpl implements NoteSemesterService{

    private final NoteModuleService noteModuleService;
    @Override
    public float calculateNoteBySession(List<ModuleResponse> moduleResponseList, String codesession) {
        float noteSemestre = 0;
        float coefficientModules = 0;
        for (ModuleResponse moduleResponse : moduleResponseList) {
            NoteModule noteModule = noteModuleService.findByCodeModuleAndCodeSession(moduleResponse.codeModule(), codesession);
            if (noteModule != null) {
                noteSemestre += noteModule.getNote() * moduleResponse.coefficientModule().floatValue();
                coefficientModules += moduleResponse.coefficientModule().floatValue();
            }
        }
          noteSemestre = noteSemestre / coefficientModules;
        return noteSemestre;
    }
}
