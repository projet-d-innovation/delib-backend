package ma.enset.departementservice.exception.handler.dto;

import lombok.Builder;

@Builder
public record ValidationExceptionResponse(
    String propertyPath,
    String error
) {}
