package ma.enset.deliberationservice.dto.session;

import lombok.Builder;

import java.util.List;

@Builder
public record SessionPagingResponse(
        int page,
        int size,
        int totalPages,
        int totalElements,
        List<SessionResponse> records
) {
}
