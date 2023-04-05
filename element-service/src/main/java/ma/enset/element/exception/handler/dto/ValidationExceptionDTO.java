package ma.enset.element.exception.handler.dto;

import lombok.Builder;

@Builder
public record ValidationExceptionDTO(
    String property,
    String message
) {}
