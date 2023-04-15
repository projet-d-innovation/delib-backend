package ma.enset.utilisateur.exception.handler.dto;

import lombok.Builder;

@Builder
public record ValidationExceptionResponse(
        String propertyPath,
        String error
) {}
