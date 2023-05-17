package ma.enset.moduleservice.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record GroupedModulesResponse(
    String codeSemestre,
    List<ModuleResponse> modules
) {
}
