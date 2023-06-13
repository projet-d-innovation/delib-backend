package ma.enset.moduleservice.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record ModulePagingResponse(
        int page,
        int size,
        int totalPages,
        long totalElements,
        List<ModuleResponse> records
) {
}
