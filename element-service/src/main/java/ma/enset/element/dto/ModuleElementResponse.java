package ma.enset.element.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record ModuleElementResponse(
        String codeModule,
        List<ElementResponse> elements

) {
}
