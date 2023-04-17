package ma.enset.departementservice.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record DepartementPagingResponse(
    int page,
    int size,
    int totalPages,
    int totalElements,
    List<DepartementResponse> records
) { }
