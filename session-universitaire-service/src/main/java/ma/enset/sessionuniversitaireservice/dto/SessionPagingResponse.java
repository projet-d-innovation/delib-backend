package ma.enset.sessionuniversitaireservice.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record SessionPagingResponse(
    int page,
    int size,
    int totalPages,
    long totalElements,
    List<SessionResponse> records
) { }
