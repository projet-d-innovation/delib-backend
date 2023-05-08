package ma.enset.sessionuniversitaireservice.exception.handler;

import com.mysql.cj.util.StringUtils;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import lombok.extern.slf4j.Slf4j;
import ma.enset.sessionuniversitaireservice.exception.BusinessException;
import ma.enset.sessionuniversitaireservice.exception.ElementAlreadyExistsException;
import ma.enset.sessionuniversitaireservice.exception.ElementNotFoundException;
import ma.enset.sessionuniversitaireservice.exception.InternalErrorException;
import ma.enset.sessionuniversitaireservice.exception.handler.dto.ExceptionResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Locale;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ElementAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ExceptionResponse> handleElementAlreadyExistsException(ElementAlreadyExistsException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                    ExceptionResponse.builder()
                        .code(HttpStatus.CONFLICT.value())
                        .status(HttpStatus.CONFLICT)
                        .message(getMessage(e))
                        .identifiers(e.getIdentifiers())
                        .build()
                );
    }

    @ExceptionHandler(ElementNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ExceptionResponse> handleElementNotFoundException(ElementNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                    ExceptionResponse.builder()
                        .code(HttpStatus.NOT_FOUND.value())
                        .status(HttpStatus.NOT_FOUND)
                        .message(getMessage(e))
                        .identifiers(e.getIdentifiers())
                        .build()
                );
    }

    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException e, HttpHeaders headers,
        HttpStatusCode status, WebRequest request) {

        return ResponseEntity
                .badRequest()
                .body(
                    ExceptionResponse.builder()
                        .code(HttpStatus.BAD_REQUEST.value())
                        .status(HttpStatus.BAD_REQUEST)
                        .errors(
                            e.getBindingResult().getFieldErrors().stream()
                                .map(fieldError -> ExceptionResponse.ValidationError.builder()
                                    .field(fieldError.getField())
                                    .message(getMessage(fieldError))
                                    .build()
                                ).toList()
                        ).build()
                );

    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionResponse> handleValidationConstraintViolationException(ConstraintViolationException e) {

        return ResponseEntity
            .badRequest()
            .body(
                ExceptionResponse.builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .status(HttpStatus.BAD_REQUEST)
                    .errors(
                        e.getConstraintViolations().stream()
                            .map(violation -> ExceptionResponse.ValidationError.builder()
                                .field(removeFirstNodeFromPropertyPath(violation.getPropertyPath()))
                                .message(violation.getMessage())
                                .build()
                            ).toList()
                    ).build()
            );
    }

    @ExceptionHandler(InternalErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ExceptionResponse> handleInternalErrorException(InternalErrorException e) {
        return ResponseEntity
                .internalServerError()
                .body(
                    ExceptionResponse.builder()
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .message(getMessage(e))
                        .build()
                );
    }

    @ExceptionHandler(NullPointerException.class)
    public void handleNullPointerException(NullPointerException e) throws InternalErrorException{
        log.error(e.getMessage(), e.getCause());
        throw new InternalErrorException();
    }

    private String removeFirstNodeFromPropertyPath(Path propertyPath) {
        StringBuilder finalPropertyPath = new StringBuilder(propertyPath.toString());

        if (propertyPath.iterator().hasNext()) {
            finalPropertyPath.delete(0, finalPropertyPath.indexOf(".") + 1);
        }

        return finalPropertyPath.toString();
    }

    private String getMessage(BusinessException exception) {
        return Objects.requireNonNull(getMessageSource()).getMessage(
            Objects.requireNonNull(exception.getKey()),
            exception.getArgs(),
            Locale.getDefault()
        );
    }

    private String getMessage(FieldError fieldError) {
        return !StringUtils.isNullOrEmpty(fieldError.getDefaultMessage()) ?
            fieldError.getDefaultMessage() :
            Objects.requireNonNull(getMessageSource()).getMessage(
                    Objects.requireNonNull(fieldError.getCode()),
                    fieldError.getArguments(),
                    Locale.getDefault()
            );
    }
}
