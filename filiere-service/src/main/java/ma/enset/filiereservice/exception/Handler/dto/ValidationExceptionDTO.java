package ma.enset.filiereservice.exception.Handler.dto;

import lombok.Builder;

@Builder
public record ValidationExceptionDTO(
    String property,
    String message
) {}
