package ma.enset.noteservice.exception.handler.dto;

import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.util.List;

@Builder
public record ExceptionResponse(
        int code,
        String status,
        String message,
        List<String> identifiers,
        List<ValidationError> errors
) {
    @Builder
    public record ValidationError(
            String field,
            String message
    ) {
    }
}