package ma.enset.inscriptionpedagogique.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record InscriptionPagingResponse(

    int page,
    int size,
    int totalPages,
    int totalElements,
    List<InscriptionResponse> records

) { }
