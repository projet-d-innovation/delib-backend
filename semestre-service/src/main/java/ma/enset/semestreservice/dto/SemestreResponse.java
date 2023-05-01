package ma.enset.semestreservice.dto;

import java.util.List;

public record SemestreResponse(
        String codeSemestre,

        String codeFiliere,

        String intituleSemestre,
        int nombreModules,
        int nombreSessions,


        List<String> modulesIds,
        List<String> sessionsIds
        ) {
}
