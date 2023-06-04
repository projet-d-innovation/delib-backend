package ma.enset.inscriptionpedagogique.exception.handler.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record ExceptionResponse (

    int code,
    String status,
    String message,
    List<String> identifiers,
    List<ValidationError> errors

) {
    @Builder
    public record ValidationError (
        String field,
        String message

    ) { }
}
