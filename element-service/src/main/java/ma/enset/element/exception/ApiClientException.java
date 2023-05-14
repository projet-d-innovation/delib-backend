package ma.enset.element.exception;

import lombok.Getter;
import ma.enset.element.exception.handler.dao.ExceptionResponse;

@Getter
public class ApiClientException extends RuntimeException {
    private final ExceptionResponse exception;

    public ApiClientException(ExceptionResponse exception) {
        super();
        this.exception = exception;
    }
}
