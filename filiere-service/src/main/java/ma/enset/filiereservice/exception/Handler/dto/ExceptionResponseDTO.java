package ma.enset.filiereservice.exception.Handler.dto;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public record ExceptionResponseDTO(
    int code,
    HttpStatus status,
    String message
) {}
