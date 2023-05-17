package ma.enset.semestreservice.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record GroupedSemestresResponse(
    String codeFiliere,
    List<SemestreResponse> modules
) {
}
