package ma.enset.noteservice.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record NoteElementPagingResponse(
    int page,
    int size,
    int totalPages,
    int totalElements,
    List<NoteElementResponse> records
) { }
