package ma.enset.filiereservice.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record GroupedSemestresResponse(
        String codeFiliere,
        List<SemestreResponse> semestres
) {
}
