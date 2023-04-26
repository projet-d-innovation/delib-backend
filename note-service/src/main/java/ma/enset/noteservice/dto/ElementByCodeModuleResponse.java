package ma.enset.noteservice.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record ElementByCodeModuleResponse(
        String codeModule,
        List<ElementResponse> elements

) {
}
