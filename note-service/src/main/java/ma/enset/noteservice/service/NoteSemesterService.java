package ma.enset.noteservice.service;

import ma.enset.noteservice.dto.ModuleResponse;

import java.util.List;

public interface NoteSemesterService {

    float calculateNoteBySession(List<ModuleResponse> moduleResponseList, String codesession);
}
