package ma.enset.moduleservice.exception.handler.dto;

import lombok.Builder;

@Builder
public record ValidationExceptionResponse(
    String property,
    String error
) {}
