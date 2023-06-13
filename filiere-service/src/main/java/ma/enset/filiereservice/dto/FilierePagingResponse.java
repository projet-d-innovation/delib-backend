package ma.enset.filiereservice.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record FilierePagingResponse(
        int page,
        int size,
        int totalPages,
        long totalElements,
        List<FiliereResponse> records
) {
}
