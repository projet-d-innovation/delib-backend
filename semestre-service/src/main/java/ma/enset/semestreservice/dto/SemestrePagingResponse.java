package ma.enset.semestreservice.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record SemestrePagingResponse(
        int page,
        int size,
        int totalPages,
        long totalElements,
        List<SemestreResponse> records
) {
}
