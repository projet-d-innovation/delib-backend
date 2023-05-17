package ma.enset.element.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record GroupedElementsResponse(
    String codeProfesseur,
    String codeModule,
    List<ElementResponse> elements
) {
}
