package ma.enset.departementservice.exception;

import lombok.Getter;
import ma.enset.departementservice.exception.handler.dto.ExceptionResponse;

@Getter
public class ApiClientException extends RuntimeException {
    private final ExceptionResponse exception;

    public ApiClientException(ExceptionResponse exception) {
        super();
        this.exception = exception;
    }
}