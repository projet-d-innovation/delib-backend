package ma.enset.utilisateur.exception.handler.dto;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public record ExceptionResponseDTO(
        int code,
        HttpStatus status,
        String message
) {
}
