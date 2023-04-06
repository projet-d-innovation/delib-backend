package ma.enset.filiereservice.exception.Handler;

import com.mysql.cj.util.StringUtils;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import ma.enset.filiereservice.constant.CoreConstants;
import ma.enset.filiereservice.exception.BusinessException;
import ma.enset.filiereservice.exception.FiliereAlreadyExistsException;
import ma.enset.filiereservice.exception.FiliereNotFoundException;
import ma.enset.filiereservice.exception.InternalErrorException;
import ma.enset.filiereservice.exception.Handler.dto.ExceptionResponseDTO;
import ma.enset.filiereservice.exception.Handler.dto.ValidationExceptionDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = FiliereAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponseDTO> handleElementAlreadyExistsException(FiliereAlreadyExistsException e) {
        return new ResponseEntity<>(
            ExceptionResponseDTO.builder()
                .code(HttpStatus.CONFLICT.value())
                .status(HttpStatus.CONFLICT)
                .message(getMessage(e))
                .build(),

            HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(value = FiliereNotFoundException.class)
    public ResponseEntity<ExceptionResponseDTO> handleElementNotFoundException(FiliereNotFoundException e) {
        return new ResponseEntity<>(
            ExceptionResponseDTO.builder()
                .code(HttpStatus.NOT_FOUND.value())
                .status(HttpStatus.NOT_FOUND)
                .message(getMessage(e))
                .build(),

            HttpStatus.NOT_FOUND
        );
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers,
                                                                  HttpStatusCode status, WebRequest request) {

        List<ValidationExceptionDTO> validationErrors = e.getBindingResult()
            .getFieldErrors().stream()
            .map(fieldError ->
                ValidationExceptionDTO.builder()
                    .property(fieldError.getField())
                    .message(getMessage(fieldError))
                    .build()
            ).toList();

        return new ResponseEntity<>(
            validationErrors,
            HttpStatus.BAD_REQUEST
        );

    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<List<ValidationExceptionDTO>> handleValidationConstraintViolationException(ConstraintViolationException e) {
        List<ValidationExceptionDTO> validationErrors = new ArrayList<>();
        String[] splitMessage;

        for (ConstraintViolation constraintViolation : e.getConstraintViolations()) {
            splitMessage = constraintViolation.getMessage().split(CoreConstants.VALIDATION_MESSAGE_SPLIT_DELIMITER);
            validationErrors.add(
                ValidationExceptionDTO.builder()
                    .property(splitMessage[0])
                    .message(splitMessage[1])
                    .build()
            );
        }

        return new ResponseEntity<>(
            validationErrors,
            HttpStatus.BAD_REQUEST
        );

//    List<ValidationExceptionDTO> validationErrors =
//        e.getConstraintViolations()
//            .stream()
//            .map(constraintViolation ->
//                ValidationExceptionDTO.builder()
//                    .property(
//                        StreamSupport.stream(constraintViolation.getPropertyPath().spliterator(), false)
//                            .reduce((first, second) -> second)
//                            .orElse(null)
//                            .getName()
//                    )
//                    .message(constraintViolation.getMessage())
//                    .build()
//                      )
//                      .toList();
    }

    @ExceptionHandler(value = InternalErrorException.class)
    public ResponseEntity<ExceptionResponseDTO> handleInternalErrorException(InternalErrorException e) {
        return new ResponseEntity<>(
            ExceptionResponseDTO.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .message(getMessage(e))
                .build(),

            HttpStatus.INTERNAL_SERVER_ERROR
        );
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
