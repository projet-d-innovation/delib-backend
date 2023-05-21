package ma.enset.element.exception.handler.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ExceptionResponse {
    private int code;
    private String status;
    private String message;
    private List<String> identifiers;
    private List<ValidationError> errors;


    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class ValidationError {
        private String field;
        private String message;
    }
}
