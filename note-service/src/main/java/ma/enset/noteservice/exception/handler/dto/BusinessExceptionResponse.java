package ma.enset.noteservice.exception.handler.dto;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public record BusinessExceptionResponse(
    int code,
    HttpStatus status,
    String error
) {}
