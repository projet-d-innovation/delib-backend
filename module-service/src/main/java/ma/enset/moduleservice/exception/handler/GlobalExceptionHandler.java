package ma.enset.moduleservice.exception.handler;

import com.mysql.cj.util.StringUtils;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import ma.enset.moduleservice.constant.CoreConstants;
import ma.enset.moduleservice.exception.BusinessException;
import ma.enset.moduleservice.exception.ElementAlreadyExistsException;
import ma.enset.moduleservice.exception.ElementNotFoundException;
import ma.enset.moduleservice.exception.InternalErrorException;
import ma.enset.moduleservice.exception.handler.dto.BusinessExceptionResponse;
import ma.enset.moduleservice.exception.handler.dto.ValidationExceptionResponse;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ElementAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<BusinessExceptionResponse> handleElementAlreadyExistsException(ElementAlreadyExistsException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(BusinessExceptionResponse.builder()
                        .code(HttpStatus.CONFLICT.value())
                        .status(HttpStatus.CONFLICT)
                        .error(getMessage(e))
                        .build());
    }

    @ExceptionHandler(ElementNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<BusinessExceptionResponse> handleElementNotFoundException(ElementNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(BusinessExceptionResponse.builder()
                        .code(HttpStatus.NOT_FOUND.value())
                        .status(HttpStatus.NOT_FOUND)
                        .error(getMessage(e))
                        .build());
    }

    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers,
                                                                  HttpStatusCode status, WebRequest request) {

        List<ValidationExceptionResponse> validationErrors = e.getBindingResult()
            .getFieldErrors().stream()
            .map(fieldError ->
                ValidationExceptionResponse.builder()
                    .property(fieldError.getField())
                    .error(getMessage(fieldError))
                    .build()
            ).toList();

        return ResponseEntity
                .badRequest()
                .body(validationErrors);

    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<List<ValidationExceptionResponse>> handleValidationConstraintViolationException(ConstraintViolationException e) {
        List<ValidationExceptionResponse> validationErrors = new ArrayList<>();
        String[] splitMessage;

        for (ConstraintViolation<?> constraintViolation : e.getConstraintViolations()) {
            splitMessage = constraintViolation.getMessage().split(CoreConstants.VALIDATION_MESSAGE_SPLIT_DELIMITER);
            validationErrors.add(
                ValidationExceptionResponse.builder()
                    .property(splitMessage[0])
                    .error(splitMessage[1])
                    .build()
            );
        }

        return ResponseEntity
                .badRequest()
                .body(validationErrors);
    }

    @ExceptionHandler(InternalErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<BusinessExceptionResponse> handleInternalErrorException(InternalErrorException e) {
        return ResponseEntity
                .internalServerError()
                .body(BusinessExceptionResponse.builder()
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .error(getMessage(e))
                        .build());
    }

    private String getMessage(BusinessException exception) {
        return Objects.requireNonNull(getMessageSource()).getMessage(
            exception.getKey(),
            exception.getArgs(),
            Locale.getDefault()
        );
    }

    private String getMessage(FieldError fieldError) {
        return !StringUtils.isNullOrEmpty(fieldError.getDefaultMessage()) ?
            fieldError.getDefaultMessage() :
            Objects.requireNonNull(getMessageSource())
                .getMessage(
                    Objects.requireNonNull(fieldError.getCode()),
                    fieldError.getArguments(),
                    Locale.getDefault()
                );
    }
}
