package ma.enset.moduleservice.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record ModulePagingResponse(
    int page,
    int size,
    int totalPages,
    int totalElements,
    List<ModuleResponse> records
) { }
