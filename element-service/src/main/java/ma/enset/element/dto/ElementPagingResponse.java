package ma.enset.element.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record ElementPagingResponse(
        int page,
        int size,
        int totalPages,
        int totalElements,
        List<ElementResponse> records
) { }
