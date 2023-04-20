package ma.enset.noteservice.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record NoteModulePagingResponse(
    int page,
    int size,
    int totalPages,
    int totalElements,
    List<NoteModuleResponse> records
) { }
