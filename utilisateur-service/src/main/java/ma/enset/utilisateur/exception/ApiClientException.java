package ma.enset.utilisateur.exception;

import lombok.Getter;
import ma.enset.utilisateur.exception.handler.dto.ExceptionResponse;

@Getter
public class ApiClientException extends RuntimeException {
    private final ExceptionResponse exception;

    public ApiClientException(ExceptionResponse exception) {
        super();
        this.exception = exception;
    }
}
