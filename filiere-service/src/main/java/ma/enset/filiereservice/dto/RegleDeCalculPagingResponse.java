package ma.enset.filiereservice.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record RegleDeCalculPagingResponse(
        int page,
        int size,
        int totalPages,
        int totalElements,
        List<RegleDeCalculResponse> records
) {
}
