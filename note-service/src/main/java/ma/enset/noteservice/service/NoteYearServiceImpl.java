package ma.enset.noteservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class NoteYearServiceImpl implements NoteYearService{
    private final NoteSemesterService noteSemesterService;


    @Override
    public float calculateNoteByCodeYear(String codeYear) {
//  List<SessionResponse> sessionResponseList =  moduleServiceFeignClient.getSessionsByCodeYear(codeyear).getBody();  TODO: the endpoint of getSessionsByCodeYear should be created in deliberation-service
//        float noteYear = 0;
//        float coefficientModules = 0;
//        for(SessionResponse sessionResponse : sessionResponseList) {                                                TODO: calculate note of session rattrapage
//            if (sessionResponse.getType() == Type.RATTRAPAGE) {
//                List<ModuleResponse> moduleResponseList = moduleServiceFeignClient.getModuleByCodeSemestre(sessionResponse.getCodeSemester()).getBody();  TODO: should get modules of each semester first
//                noteYear += noteSemesterService.calculateNoteBySession(moduleResponseList, sessionResponse.getCodeSession());    TODO: calculate note by code session
//            }
//        }
//        return noteYear / 2;
        return 0;
    }


}
