package ma.enset.semestreservice.exception.Handler.dto;

import lombok.Builder;

@Builder
public record ValidationExceptionDTO(
    String property,
    String message
) {}
