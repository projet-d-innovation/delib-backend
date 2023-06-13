package ma.enset.utilisateur.dto;

import lombok.Builder;

import java.util.List;

@Builder

public record PagingResponse<T> (
    int page,
    int size,
    int totalPages,
    long totalElements,
    List<T> records

){}
