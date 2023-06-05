package ma.enset.noteservice.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record GroupedElementsResponse(
        String codeModule,
        List<ElementResponse> elements
) {
}
