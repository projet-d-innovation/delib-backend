package ma.enset.filiereservice.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record ReglePagingResponse(
    int page,
    int size,
    int totalPages,
    int totalElements,
    List<RegleResponse> records
) { }
